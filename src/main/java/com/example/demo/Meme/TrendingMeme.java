package com.example.demo.Meme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendingMeme {
    private Long id;
    private String title;
    private String caption;
    private String imageUrl;
    private String username; 
    private Integer voteCount;
    private Integer commentCount;
}