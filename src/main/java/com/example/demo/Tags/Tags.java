package com.example.demo.Tags;

public class Tags {
    private Long id;
    private String name;

    // Default constructor
    public Tags() {
    }

    // Constructor with name only (for new tags)
    public Tags(String name) {
        this.name = name;
    }

    // Constructor with id and name (full constructor)
    public Tags(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
