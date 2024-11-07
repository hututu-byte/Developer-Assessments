package com.example.developerassessmentsmaster.model;

import lombok.Data;

@Data
public class ProjectDetailInfo {
    private String name;
    private String description;
    private Integer stars;
    private String language;

    public ProjectDetailInfo(String name, String description, int stars, String language) {
        this.name = name;
        this.description = description;
        this.stars = stars;
        this.language = language;
    }
}
