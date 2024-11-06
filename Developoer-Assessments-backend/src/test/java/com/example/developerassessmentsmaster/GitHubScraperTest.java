package com.example.developerassessmentsmaster;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.model.TalentRank;
import com.example.developerassessmentsmaster.service.DeveloperService;
import com.example.developerassessmentsmaster.scraper.GitHubScraper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 爬取数据测试类
 */
@SpringBootTest
public class GitHubScraperTest {

    @Autowired
    private GitHubScraper scraper;

    @Autowired
    private DeveloperService developerService;

    @Test
    public void testFetchDevelopersAndSave() {
        try {
            JSONArray developers = scraper.fetchDevelopers("springboot", 30);
            assertNotNull(developers);
            assertTrue(developers.length() > 0);

            for (int i = 0; i < developers.length(); i++) {
                JSONObject devJson = developers.getJSONObject(i);
                Developer developer = new Developer();
                developer.setGithubId(devJson.getLong("id"));
                developer.setGithubUsername(devJson.getString("login"));

                // Fetch additional details for developer
                JSONObject details = scraper.fetchDeveloperDetails(developer.getGithubUsername());
                String country = details.optString("location", "");

                // 如果国家信息缺失，尝试使用推测的逻辑
                if (country == null || country.isEmpty()) {
                    // 使用语言等其他信息推测
                    country = scraper.guessCountry(details, Arrays.asList("java", "python")); // 假设使用一些项目语言进行推测
                }

                developer.setCountry(country);
                developer.setTalentRank(TalentRank.LOW); // 假设初始为 LOW，可以根据实际情况调整
                developer.setBio(details.optString("bio", ""));
                developer.setFollowing(details.optInt("following", 0));
                developer.setFollowers(details.optInt("followers", 0));

                // Save developer
                developerService.addDeveloper(developer);
                Long developerId = developer.getGithubId();
                assertNotNull(developerId, "Developer ID should not be null after saving");

                // Fetch and save projects
                JSONArray projects = scraper.fetchProjects(devJson.getString("login"));
                assertNotNull(projects);

                for (int j = 0; j < projects.length(); j++) {
                    JSONObject projJson = projects.getJSONObject(j);
                    Project project = new Project();
                    project.setGithubId(projJson.getLong("id"));
                    project.setName(projJson.getString("name"));
                    project.setStars(projJson.getInt("stargazers_count"));
                    project.setForks(projJson.getInt("forks_count"));
                    project.setDescription(projJson.optString("description", ""));
                    project.setUrl(projJson.optString("html_url", ""));
                    project.setCreatorId(developerId); // Set owner ID
                    project.setLanguage(projJson.optString("language", "")); // 获取语言标签

                    developerService.saveProject(project);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
