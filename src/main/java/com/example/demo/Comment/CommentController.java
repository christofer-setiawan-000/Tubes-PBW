package com.example.demo.Comment;

import com.example.demo.User.User;
import com.example.demo.common.RequiredRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    private JdbcCommentRepository commentRepository;

    @PostMapping("/memes/{memeId}/comment")
    @RequiredRole({"user", "moderator"}) // Guest tidak boleh komen
    public String addComment(@PathVariable Long memeId, 
                             @RequestParam String content, 
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        // Simpan
        Comment comment = new Comment(null, memeId, user.getUsername(), content, null);
        commentRepository.save(comment);

        return "redirect:/memes/" + memeId;
    }

    @PostMapping("/comments/{id}/delete")
    @RequiredRole({"user", "moderator"})
    public String deleteComment(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Comment> commentOpt = commentRepository.findById(id);

        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            // Cek otorisasi: Boleh hapus jika dia pemilik ATAU dia moderator
            if (comment.getUsername().equals(user.getUsername()) || "moderator".equals(user.getRole())) {
                commentRepository.deleteById(id);
                return "redirect:/memes/" + comment.getMemeId();
            }
        }
        return "redirect:/dashboard";
    }

    

    // 1. TAMPILKAN HALAMAN EDIT (GET)
    @GetMapping("/comments/{id}/edit")
    @RequiredRole({"user", "moderator"})
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        // Cari komentar berdasarkan ID
        Optional<Comment> commentOpt = commentRepository.findById(id);
        
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            
            if (comment.getUsername().equals(user.getUsername())) {
                model.addAttribute("comment", comment);
                model.addAttribute("user", user);
                return "edit";
            }
        }
        

        return "redirect:/dashboard";
    }

    // 2. PROSES SIMPAN PERUBAHAN (POST)
    @PostMapping("/comments/{id}/update")
    @RequiredRole({"user", "moderator"})
    public String updateComment(@PathVariable Long id, 
                                @RequestParam String content, 
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Comment> commentOpt = commentRepository.findById(id);

        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            
            if (comment.getUsername().equals(user.getUsername())) {
                commentRepository.update(id, content);
                
                // Redirect balik ke Meme yang bersangkutan
                return "redirect:/memes/" + comment.getMemeId();
            }
        }
        return "redirect:/dashboard";
    }
}