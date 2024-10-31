package com.example.developerassessmentsmaster.utils;


import com.example.developerassessmentsmaster.model.Developer;

import java.util.List;


/**
 * 计算rank值
 */
public class TalentRankCalculator {
    public static void calculateTalentRank(List<Developer> developers) {
        // 计算 TalentRank 的逻辑
        for (Developer developer : developers) {
            // 根据项目的重要性和贡献度计算排名
            developer.setTalentRank(Math.random()); // 示例：随机生成
        }
    }
}
