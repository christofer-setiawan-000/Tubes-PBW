package com.example.demo.Meme;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Comment.Comment;
import com.example.demo.Comment.JdbcCommentRepository;
import com.example.demo.User.User;
import com.example.demo.common.RequiredRole;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemeController {
    //-------------testing---------
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcCommentRepository commentRepository;
    
    List<String> categoryList = List.of("Programming", "Animals", "Relatable", "Work", "Classic");
    List<String> tagList = List.of("funny", "code", "cat", "monday", "chaos", "reaction", "classic");


    //-------------testing---------
    @GetMapping("/memes")
    @RequiredRole("*")
    public String memeFeed(@RequestParam(value = "category", required = false) String selectedCategory,
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        // Hardcoded categories list
        model.addAttribute("categories", categoryList);
        model.addAttribute("selectedCategory", selectedCategory);
        
        try {
            String sql;
            List<Meme> memes;
            
            // Base SQL structure, including the necessary comment_count subquery
            String baseSelect = """
                SELECT m.*, 
                (SELECT COUNT(*) FROM comments comm WHERE comm.meme_id = m.id) AS commentCount 
                FROM memes m 
            """;
            
            // Filter by category if provided, otherwise show all
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                // Query for filtering by category, joining memes, meme_categories, and categories tables.
                // CRITICAL FIX: The baseSelect ensures comment_count is included here.
                sql = baseSelect +
                      "INNER JOIN meme_categories mc ON m.id = mc.meme_id " +
                      "INNER JOIN categories c ON mc.category_id = c.id " +
                      "WHERE c.name = ? ORDER BY m.created_at DESC";
                memes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Meme.class), selectedCategory);
            } else {
                // Query to fetch all memes, ordered by date
                sql = baseSelect + "ORDER BY m.created_at DESC";
                memes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Meme.class));
            }
            
            // Fetch tags and primary category for each meme (this logic remains the same)
            Map<Long, List<String>> memeTags = new HashMap<>();
            for (Meme meme : memes) {
                // Fetch tags for each meme
                String tagsSql = "SELECT t.name FROM tags t " +
                                 "INNER JOIN meme_tags mt ON t.id = mt.tag_id " +
                                 "WHERE mt.meme_id = ? ORDER BY t.name ASC";
                List<String> tags = jdbcTemplate.queryForList(tagsSql, String.class, meme.getId());
                memeTags.put(meme.getId(), tags);
                
                // Fetch first category for this meme
                try {
                    String catSql = "SELECT c.name FROM categories c INNER JOIN meme_categories mc ON c.id = mc.category_id WHERE mc.meme_id = ? ORDER BY c.name ASC LIMIT 1";
                    String cat = jdbcTemplate.queryForObject(catSql, String.class, meme.getId());
                    if (cat != null) {
                        meme.setCategory(cat);
                    }
                } catch (Exception ex) {
                    // ignore if no category found
                }
            }
            
            model.addAttribute("memes", memes);
            model.addAttribute("memeTags", memeTags);
        } catch (Exception e) {
            System.err.println("Error fetching memes: " + e.getMessage());
            model.addAttribute("memes", List.of());
            model.addAttribute("memeTags", new HashMap<>());
        }
        return "memes/feed";
    }

    @GetMapping("/memes/upload")
    @RequiredRole({"user", "moderator"})
    public String uploadMeme(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryList);
        model.addAttribute("tags", tagList);
        
        return "memes/upload";
    }

    @PostMapping("/memes/upload")
    @RequiredRole({"user", "moderator"})
    public String handleUpload(@RequestParam("title") String title,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "tags", required = false) String[] tags,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirectAttrs) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (file == null || file.isEmpty()) {
            redirectAttrs.addFlashAttribute("uploadStatus", "no-file");
            return "redirect:/memes/upload";
        }

        try {
            // Use the JVM working directory explicitly to avoid Tomcat temp dir issues
            Path dataDir = Paths.get(System.getProperty("user.dir"), "data");
            Files.createDirectories(dataDir);

            String original = file.getOriginalFilename();
            String safeName = System.currentTimeMillis() + "" + (original == null ? "file" : original.replaceAll("[^a-zA-Z0-9.\\-]", "_"));
            Path target = dataDir.resolve(safeName).toAbsolutePath();

            // Save file to disk using stream copy (more robust across filesystems)
            try (var in = file.getInputStream()) {
                Files.copy(in, target);
            }

            String imageUrl = "/data/" + safeName;

            // Perform DB operations inside a transaction. If anything fails, the transaction will rollback
            TransactionTemplate tx = new TransactionTemplate(transactionManager);
            try {
                Long memeId = tx.execute(status -> {
                    String insertMemeSQL = "INSERT INTO memes (title, image_url, username) VALUES (?, ?, ?) RETURNING id";
                    Long id = jdbcTemplate.queryForObject(insertMemeSQL, Long.class, title, imageUrl, user.getUsername());

                    // Insert category association if provided. Create category if it doesn't exist.
                    if (category != null && !category.isEmpty()) {
                        Long categoryId = null;
                        try {
                            categoryId = jdbcTemplate.queryForObject("SELECT id FROM categories WHERE name = ?", Long.class, category);
                        } catch (Exception ex) {
                            // not found -> create
                            categoryId = jdbcTemplate.queryForObject("INSERT INTO categories (name) VALUES (?) RETURNING id", Long.class, category);
                        }
                        if (categoryId != null) {
                            jdbcTemplate.update("INSERT INTO meme_categories (meme_id, category_id) VALUES (?, ?)", id, categoryId);
                        }
                    }

                    // Insert tag associations if provided. Create tags if they don't exist.
                    if (tags != null && tags.length > 0) {
                        for (String tag : tags) {
                            if (tag != null && !tag.isEmpty()) {
                                Long tagId = null;
                                try {
                                    tagId = jdbcTemplate.queryForObject("SELECT id FROM tags WHERE name = ?", Long.class, tag);
                                } catch (Exception ex) {
                                    // not found -> create
                                    tagId = jdbcTemplate.queryForObject("INSERT INTO tags (name) VALUES (?) RETURNING id", Long.class, tag);
                                }
                                if (tagId != null) {
                                    jdbcTemplate.update("INSERT INTO meme_tags (meme_id, tag_id) VALUES (?, ?)", id, tagId);
                                }
                            }
                        }
                    }
                    return id;
                });

                redirectAttrs.addFlashAttribute("uploadStatus", "success");
                return "redirect:/memes";
            } catch (Exception dbEx) {
                // DB transaction failed and was rolled back — delete the saved file to avoid orphaned files
                try {
                    Files.deleteIfExists(target);
                } catch (Exception delEx) {
                    // ignore deletion failures
                }
                // rethrow so outer catch can handle redirect attrs and logging
                throw dbEx;
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("uploadStatus", "error");
            redirectAttrs.addFlashAttribute("uploadError", e.getMessage());
            return "redirect:/memes/upload";
        }
    }

    //-----------testing-----------
    @GetMapping("/memes/{id}")
    @RequiredRole("*")
    public String memeDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // Cek session biar aman (Anti Crash)
        if (user == null) {
             model.addAttribute("user", null); 
        } else {
             model.addAttribute("user", user);
        }

        try {
            String sql = "SELECT * FROM memes WHERE id = ?";
            Meme meme = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Meme.class), id);
            model.addAttribute("meme", meme);
            
            // Fetch category for this meme (first category if multiple)
            try {
                String catSql = "SELECT c.name FROM categories c INNER JOIN meme_categories mc ON c.id = mc.category_id WHERE mc.meme_id = ? LIMIT 1";
                String memeCategory = jdbcTemplate.queryForObject(catSql, String.class, id);
                if (memeCategory != null) {
                    meme.setCategory(memeCategory);
                }
            } catch (Exception ex) {
                // no category found - ignore
            }

            // Fetch tags for this meme from meme_tags junction table
            String tagsSql = "SELECT t.name FROM tags t " +
                            "INNER JOIN meme_tags mt ON t.id = mt.tag_id " +
                            "WHERE mt.meme_id = ? ORDER BY t.name ASC";
            List<String> tags = jdbcTemplate.queryForList(tagsSql, String.class, id);
            model.addAttribute("tags", tags);
            
            // --- LANGKAH 2: AMBIL DATA KOMENTAR ---
            List<Comment> comments = commentRepository.findByMemeId(id);
            model.addAttribute("comments", comments);

            return "memes/detail";

        } catch (Exception e) {
            System.out.println("Error ambil meme: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}