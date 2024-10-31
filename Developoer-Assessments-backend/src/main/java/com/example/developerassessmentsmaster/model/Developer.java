package com.example.developerassessmentsmaster.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Developer {
    private Long id; // 开发者ID
    private Long githubId; // GitHub用户ID
    private String githubUsername; // GitHub用户名
    private String name; // 开发者姓名
    private String email; // 开发者电子邮件
    private Long nationId; // 外键，关联国家
    private Double talentRank; // 开发者技术能力评分
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

}
