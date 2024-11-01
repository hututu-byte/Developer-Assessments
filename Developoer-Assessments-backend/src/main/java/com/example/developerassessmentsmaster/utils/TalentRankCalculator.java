package com.example.developerassessmentsmaster.utils;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;

import java.util.List;

public class TalentRankCalculator {
    public static void calculateTalentRank(List<Developer> developers) {
        for (Developer developer : developers) {
            double score = 0;
            // 假设获取开发者的项目并计算
            for (Project project : developer.getProjects()) {
                score += project.getStars() * 0.5; // 示例权重
            }
            developer.setTalentRank(score);
        }
    }
}
