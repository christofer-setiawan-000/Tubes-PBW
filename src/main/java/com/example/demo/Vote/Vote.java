package com.example.demo.Vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Vote {
    
    private Long id;
    
    private Long memeId;
    
    private String username;
    
    private LocalDateTime votedAt;
}
