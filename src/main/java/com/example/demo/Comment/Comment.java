package com.example.demo.Comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Comment {
    
    private Long id;
    
    private Long memeId;
    
    private String username;
    
    @NotNull
    @Size(min = 1, max = 500)
    private String content;
    
    private LocalDateTime createdAt;
}
