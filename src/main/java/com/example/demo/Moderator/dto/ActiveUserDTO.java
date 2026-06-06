package com.example.demo.Moderator.dto;

public class ActiveUserDTO {
    private String username;
    private int commentsCount;
    private int votesCount;
    private int activityScore;

    public ActiveUserDTO() {}
    public ActiveUserDTO(String username, int commentsCount, int votesCount, int activityScore) {
        this.username = username;
        this.commentsCount = commentsCount;
        this.votesCount = votesCount;
        this.activityScore = activityScore;
    }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }
    public int getVotesCount() { return votesCount; }
    public void setVotesCount(int votesCount) { this.votesCount = votesCount; }
    public int getActivityScore() { return activityScore; }
    public void setActivityScore(int activityScore) { this.activityScore = activityScore; }
}
