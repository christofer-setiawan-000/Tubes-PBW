package com.example.demo.Moderator.dto;

public class TrendingMemeDTO {
    private long memeId;
    private String title;
    private int votes;

    public TrendingMemeDTO() {}
    public TrendingMemeDTO(long memeId, String title, int votes) {
        this.memeId = memeId;
        this.title = title;
        this.votes = votes;
    }
    public long getMemeId() { return memeId; }
    public void setMemeId(long memeId) { this.memeId = memeId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }
}
