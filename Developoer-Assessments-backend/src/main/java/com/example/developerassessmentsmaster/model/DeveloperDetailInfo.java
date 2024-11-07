package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.util.List;

@Data
public class DeveloperDetailInfo {
    private Long githubId;
    private String githubUsername;
    private int followers;
    private int following;
    private String country;
    private String bio;
    private int totalStars;
    private int totalForks;
    private int totalProjects; // 新增字段
    private List<ProjectDetailInfo> topProjects;
}
