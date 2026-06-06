package com.example.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User login(String usernameOrEmail, String password) {
        // Mencari username terlebih dahulu
        var user = userRepository.findByUsername(usernameOrEmail);
        
        // Jika tidak ditemukan, coba cari berdasarkan email
        if (user.isEmpty()) {
            user = userRepository.findByEmail(usernameOrEmail);
        }
        
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.get();
            }
        }
        
        return null;
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
