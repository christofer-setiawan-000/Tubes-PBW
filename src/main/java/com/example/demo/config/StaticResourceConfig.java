package com.example.demo.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files under project-root `data/` directory at URL path /data/**
        String dataPath = Paths.get(System.getProperty("user.dir"), "data").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/data/**")
                .addResourceLocations(dataPath)
                .setCachePeriod(3600);
    }
}
