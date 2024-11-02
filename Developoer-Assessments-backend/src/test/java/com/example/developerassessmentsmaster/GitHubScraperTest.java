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
            JSONArray developers = scraper.fetchDevelopers("java", 5);
            assertNotNull(developers);
            assertTrue(developers.length() > 0);

            for (int i = 0; i < developers.length(); i++) {
                JSONObject devJson = developers.getJSONObject(i);
                Developer developer = new Developer();
                developer.setGithubId(devJson.getLong("id"));
                developer.setGithubUsername(devJson.getString("login"));

                // Fetch additional details for developer
                JSONObject details = scraper.fetchDeveloperDetails(developer.getGithubId());
                developer.setCountry(details.optString("location", ""));
                developer.setTalentRank(0.0); // You can implement a way to calculate this later
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
