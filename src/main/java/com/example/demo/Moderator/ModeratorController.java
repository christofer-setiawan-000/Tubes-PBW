package com.example.demo.Moderator;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.User.User;
import com.example.demo.User.UserRepository; 
import com.example.demo.common.RequiredRole;

import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate; 

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/users/delete")
    @RequiredRole("moderator")
    public String deleteUser(@RequestParam String username) {
        jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
        
        return "redirect:/moderator/users";
    }

    @GetMapping("/users")
    @RequiredRole("moderator")
    public String manageUsers(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user == null) {
            return "redirect:/login";
        }


        //pagination
        int pageSize = 5;
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("userPage", userPage); // Objek Page (bukan List lagi)
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "moderator/users";
    }

    // @GetMapping("/reports")
    // @RequiredRole("moderator")
    // public String reports(HttpSession session, Model model) {
    //     User user = (User) session.getAttribute("user");
    //     model.addAttribute("user", user);
    //     return "moderator/reports";
    // }

    @PostMapping("/meme/{id}/toggle-comments") 
    @RequiredRole("moderator")
    public String toggleComments(@PathVariable Long id, @RequestParam boolean enabled) {
        // Logic update database
        String sql = "UPDATE memes SET comments_enabled = ? WHERE id = ?";
        jdbcTemplate.update(sql, enabled, id);
        
        // Balik lagi ke halaman detail meme
        return "redirect:/memes/" + id;
    }
}
