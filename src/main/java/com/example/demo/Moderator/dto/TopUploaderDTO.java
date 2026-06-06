package com.example.demo.Moderator.dto;

public class TopUploaderDTO {
    private String username;
    private String name;
    private int totalUploaded;

    public TopUploaderDTO() {}
    public TopUploaderDTO(String username, String name, int totalUploaded) {
        this.username = username;
        this.name = name;
        this.totalUploaded = totalUploaded;
    }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalUploaded() { return totalUploaded; }
    public void setTotalUploaded(int totalUploaded) { this.totalUploaded = totalUploaded; }
}
