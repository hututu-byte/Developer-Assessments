package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Project {
    private Long githubId; // 项目ID
//    private Long ownerId; // 项目拥有者
    private String name; // 项目名称
    private int stars; // 星标数
    private int forks; // 分叉数
    private String description; // 项目描述
    private String url; // 项目地址
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
