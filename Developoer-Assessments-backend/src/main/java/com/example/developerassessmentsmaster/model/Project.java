package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Project {
    private Long Id; //主键ID
    private Long githubId; // 项目ID
    private Long creatorId; // 项目拥有者的GitHub ID
    private String name; // 项目名称
    private int stars; // 星标数
    private int forks; // 分叉数
    private String description; // 项目描述
    private String url; // 项目地址
    private String language; // 项目语言标签
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
