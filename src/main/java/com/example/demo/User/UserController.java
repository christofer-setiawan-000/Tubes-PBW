package com.example.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerView(User user){
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        if (!user.getPassword().equals(user.getConfirmpassword())) {
            bindingResult.rejectValue("confirmpassword", "PasswordMismatch", "Password do not match");
            return "register";
        }
        
        // Cek apakah email sudah pernah terdaftar
        if (userService.isEmailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "EmailExists", "Email sudah terdaftar");
            return "register";
        }
        
        boolean isRegistered = userService.register(user);
        
        if (!isRegistered) {
            bindingResult.rejectValue("username", "RegistrationFailed", "Failed to register user. Username may already exist or database error occurred");
            return "register";
        }
        
        return "redirect:/results";
    }

    @GetMapping("/results")
    public String resultsView(){
        return "results";
    }
    
    
}