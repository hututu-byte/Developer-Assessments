package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Developer {
    private Long id; // 开发者ID
    private Long githubId; // GitHub用户ID
    private String githubUsername; // GitHub用户名
    private String country; // 所属国家/地区
    private Double talentRank; // 开发者技术能力评分
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    private List<Project> projects; // 开发者的项目列表
}
