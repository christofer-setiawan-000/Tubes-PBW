package com.example.demo.Meme;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//-----------testing--------
@NoArgsConstructor
public class Meme {
    
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 200)
    private String title;
    
    @Size(max = 500)
    private String caption;
    
    @NotNull
    private String imageUrl;
    
    @NotNull
    private String username; // uploader
    
    private String category;
    
    private Integer voteCount;
    
    private Boolean commentsEnabled;
    
    private LocalDateTime createdAt;

    private Integer commentCount;
}
