package com.example.developerassessmentsmaster.model;

import lombok.Data;

@Data
public class LanguageCount {
    private Long githubId;
    private String language; // 最常见标签
    private int tagCount;    // 标签出现的次数
}
