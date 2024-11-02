package com.example.developerassessmentsmaster.service;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;
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
            developer.setTalentRank(0.0);
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
                project.setCreatorId(developer.getGithubId()); // 使用 creatorId 作为项目拥有者ID
                saveProject(project);
            }
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
