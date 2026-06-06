package com.example.demo.Moderator;

import com.example.demo.common.RequiredRole;
import com.example.demo.User.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/moderator/reports")
    @RequiredRole("moderator")
    public String reportsHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Reports Dashboard");
        return "moderator/reports";
    }

    @GetMapping("/moderator/reports/chart")
    @RequiredRole("moderator")
    public String chartReport(HttpSession session, Model model) {
        model.addAttribute("title", "Chart Report");
        return "moderator/chart-report";
    }

    @GetMapping("/moderator/reports/pdf")
    @RequiredRole("moderator")
    public String pdfReport(HttpSession session, Model model) {
        model.addAttribute("title", "PDF Report");
        return "moderator/pdf-report";
    }
}
