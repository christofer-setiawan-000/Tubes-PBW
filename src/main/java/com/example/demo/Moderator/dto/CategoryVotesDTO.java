package com.example.demo.Moderator.dto;

public class CategoryVotesDTO {
    private String category;
    private int totalVotes;

    public CategoryVotesDTO() {}
    public CategoryVotesDTO(String category, int totalVotes) {
        this.category = category;
        this.totalVotes = totalVotes;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalVotes() { return totalVotes; }
    public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }
}
