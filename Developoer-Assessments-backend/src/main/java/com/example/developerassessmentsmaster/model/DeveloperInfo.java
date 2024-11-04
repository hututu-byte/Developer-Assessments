package com.example.developerassessmentsmaster.model;


import lombok.Data;

@Data
public class DeveloperInfo {
    private String githubUsername;
    private String talentRank;
    private String bio;
    private String country;
    //标签，为project中的language
    private String mostCommonTag;
}
