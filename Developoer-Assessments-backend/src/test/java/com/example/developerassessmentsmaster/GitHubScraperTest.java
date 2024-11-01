package com.example.developerassessmentsmaster;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.service.DeveloperService;
import com.example.developerassessmentsmaster.scraper.GitHubScraper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GitHubScraperTest {

    @Autowired
    private GitHubScraper scraper;

    @Autowired
    private DeveloperService developerService;

    @Test
    public void testFetchDevelopersAndSave() {
        try {
            JSONArray developers = scraper.fetchDevelopers("springboot", 5);
            assertNotNull(developers);
            assertTrue(developers.length() > 0);

            for (int i = 0; i < developers.length(); i++) {
                JSONObject devJson = developers.getJSONObject(i);
                Developer developer = new Developer();
                developer.setGithubId(devJson.getLong("id"));
                developer.setGithubUsername(devJson.getString("login"));
                developer.setCountry(devJson.optString("country", ""));
                developer.setTalentRank(devJson.optDouble("talent_rank", 0.0));

                // 保存开发者并确保获取到ID
                developerService.addDeveloper(developer);
                Long developerId = developer.getGithubId(); // 获取开发者的ID
                assertNotNull(developerId, "Developer ID should not be null after saving");

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
                    project.setUrl(projJson.optString("html_url", "")); // 添加项目地址

                    developerService.saveProject(project);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
