package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class UploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        long maxFileSize = 10 * 1024 * 1024; // 10 MB
        long maxRequestSize = 10 * 1024 * 1024; // 10 MB
        return new MultipartConfigElement("", maxFileSize, maxRequestSize, 0);
    }
}
