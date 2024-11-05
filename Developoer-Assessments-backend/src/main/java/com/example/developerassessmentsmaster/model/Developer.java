package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Developer {
    private Long id; // 主键ID
    private Long githubId; // GitHub用户ID
    private String githubUsername; // GitHub用户名
    private String country; // 所属国家/地区
    private TalentRank talentRank; // 开发者技术能力评分
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    private String bio; // 开发者个人简介
    private int following; // 开发者关注人数
    private int followers; // 开发者粉丝人数
    private List<Project> projects; // 开发者的项目列表
    private Double Score;
}
