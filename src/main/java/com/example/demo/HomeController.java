package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.User.User;
import com.example.demo.common.RequiredRole;
import com.example.demo.Meme.TrendingMeme;

import jakarta.servlet.http.HttpSession;
import java.util.List; 


@Controller
public class HomeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String landingPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/trending")
    public String homepage(Model model) {
        String sql = "SELECT memes.*, " +
                     "(SELECT COUNT(*) FROM comments WHERE comments.meme_id = memes.id) as commentCount " +
                     "FROM memes " +
                     "ORDER BY memes.vote_count DESC " +
                     "LIMIT 10";
        
        try {
            List<TrendingMeme> trendingMemes = jdbcTemplate.query(
                sql, 
                new BeanPropertyRowMapper<>(TrendingMeme.class)
            );
            model.addAttribute("trendingMemes", trendingMemes);
        } catch (Exception e) {
            model.addAttribute("trendingMemes", List.of());
        }
        return "homepage";
    }

    @GetMapping("/public")
    @RequiredRole({"user", "moderator"})
    public String publicPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "public";
    }

    @GetMapping("/moderator")
    @RequiredRole({"moderator"})
    public String moderatorPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "moderator";
    }
}
