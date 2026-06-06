package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Meme.Meme;
import com.example.demo.User.User;
import com.example.demo.User.UserService;
import com.example.demo.common.RequiredRole;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/login")
    public String loginView(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            //if tambahan supaya ketika user udah login sebagai moderator langsung masuk ke page moderator
            if("moderator".equals(user.getRole())){
                return "redirect:/dashboard";
            }
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       HttpSession session, Model model) {
        User user = userService.login(username, password);
        
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            //buat mengecek masuk sebagai apa moderator? member biasa
            if("moderator".equals(user.getRole())){
                return "redirect:/dashboard";
            }
            else{
                return "redirect:/dashboard";
            }
        } else {
            model.addAttribute("status", "failed");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    @RequiredRole("*")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        // Fetch memes uploaded by the current user
        if (user != null) {
            try {
                String sql = "SELECT * FROM memes WHERE username = ? ORDER BY created_at DESC";
                List<Meme> userMemes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Meme.class), user.getUsername());
                model.addAttribute("userMemes", userMemes);
            } catch (Exception e) {
                model.addAttribute("userMemes", List.of());
            }
        } else {
            model.addAttribute("userMemes", List.of());
        }
        
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("username");
        session.removeAttribute("role");
        return "redirect:/";
    }

}
