package com.example.developerassessmentsmaster.service;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.model.TalentRank;
import com.example.developerassessmentsmaster.repository.DeveloperMapper;
import com.example.developerassessmentsmaster.repository.ProjectMapper;
import com.example.developerassessmentsmaster.scraper.GitHubScraper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeveloperService {
    @Autowired
    private DeveloperMapper developerMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private GitHubScraper gitHubScraper;

    public void addDeveloper(Developer developer) {
        developerMapper.insertDeveloper(developer);
    }

    public void saveDevelopers(JSONArray developers) {
        for (int i = 0; i < developers.length(); i++) {
            JSONObject devJson = developers.getJSONObject(i);
            Developer developer = new Developer();
            developer.setGithubId(devJson.getLong("id"));
            developer.setGithubUsername(devJson.getString("login"));
            developer.setCountry(devJson.optString("location", ""));
            developer.setBio(devJson.optString("bio", ""));
            developer.setFollowing(devJson.optInt("following", 0));
            developer.setFollowers(devJson.optInt("followers", 0));
            developer.setCreatedAt(LocalDateTime.now());
            developer.setUpdatedAt(LocalDateTime.now());

            // 保存开发者
            developerMapper.insertDeveloper(developer);

            // 获取并保存开发者的项目
            List<Project> projects = getDeveloperProjects(developer.getGithubUsername());
            for (Project project : projects) {
                project.setCreatorId(developer.getGithubId());
                saveProject(project);
            }

            // 更新开发者的 TalentRank
            updateDeveloperTalentRank(developer.getId());
        }
    }

    public void updateDeveloperTalentRank(Long githubId) {
        Developer developer = developerMapper.getDeveloperByGithubId(githubId);
        if (developer == null) {
            throw new IllegalArgumentException("Developer not found with github_id: " + githubId);
        }

        // 打印开发者信息以调试
        System.out.println("Updating developer: " + developer.getGithubUsername());

        // 获取开发者的项目
        List<Project> projects = projectMapper.getProjectsByCreatorId(developer.getGithubId());

        // 计算总星标和分叉数
        int totalStars = projects.stream().mapToInt(Project::getStars).sum();
        int totalForks = projects.stream().mapToInt(Project::getForks).sum();

        // 更新 TalentRank
        developer.setTalentRank(calculateTalentRank(developer, totalStars, totalForks));
        developerMapper.updateDeveloperByGithubId(developer);
    }

    private TalentRank calculateTalentRank(Developer developer, int totalStars, int totalForks) {
        double score = (developer.getFollowers() * 0.4) + (totalStars * 0.4) + (totalForks * 0.2);
        if (score < 30) {
            return TalentRank.LOW;
        } else if (score < 70) {
            return TalentRank.MEDIUM;
        } else {
            return TalentRank.HIGH;
        }
    }

    public List<Project> getDeveloperProjects(String username) {
        try {
            JSONArray projectsJson = gitHubScraper.fetchProjects(username);
            List<Project> projects = new ArrayList<>();

            for (int i = 0; i < projectsJson.length(); i++) {
                JSONObject projectJson = projectsJson.getJSONObject(i);
                Project project = new Project();
                project.setGithubId(projectJson.getLong("id"));
                project.setCreatorId(projectJson.getLong("owner_id")); // 设置所有者开发者的ID
                project.setName(projectJson.getString("name"));
                project.setStars(projectJson.getInt("stargazers_count"));
                project.setForks(projectJson.getInt("forks_count"));
                project.setDescription(projectJson.optString("description", ""));
                project.setLanguage(projectJson.optString("language", "")); // 获取语言标签

                projects.add(project);
            }

            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveProject(Project project) {
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        projectMapper.insertProject(project);
    }
}
