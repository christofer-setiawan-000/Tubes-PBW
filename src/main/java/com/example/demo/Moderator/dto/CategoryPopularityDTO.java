package com.example.demo.Moderator.dto;

public class CategoryPopularityDTO {
    private String category;
    private int totalMemes;

    public CategoryPopularityDTO() {}
    public CategoryPopularityDTO(String category, int totalMemes) {
        this.category = category;
        this.totalMemes = totalMemes;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalMemes() { return totalMemes; }
    public void setTotalMemes(int totalMemes) { this.totalMemes = totalMemes; }
}
