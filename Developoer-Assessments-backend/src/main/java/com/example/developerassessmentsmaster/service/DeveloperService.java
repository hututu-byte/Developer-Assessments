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

    public List<Developer> getDevelopers() {
        return developerMapper.getAllDevelopers();
    }

    public void addDeveloper(Developer developer) {
        developerMapper.insertDeveloper(developer);
    }

    public void saveDevelopers(JSONArray developers) {
        for (int i = 0; i < developers.length(); i++) {
            JSONObject devJson = developers.getJSONObject(i);
            Developer developer = new Developer();
            developer.setGithubId(devJson.getLong("id"));
            developer.setGithubUsername(devJson.getString("login"));
            developer.setCountry(devJson.optString("country")); // 添加国家字段
            developer.setTalentRank(devJson.optDouble("talent_rank", 0.0)); // 添加技术能力评级字段
            developer.setCreatedAt(LocalDateTime.now());
            developer.setUpdatedAt(LocalDateTime.now());
            developerMapper.insertDeveloper(developer);
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
//                project.setOwnerId(projectJson.getLong("owner_id")); // 设置所有者开发者的ID
                project.setName(projectJson.getString("name"));
                project.setStars(projectJson.getInt("stargazers_count"));
                project.setForks(projectJson.getInt("forks_count"));
                project.setDescription(projectJson.optString("description", ""));

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
