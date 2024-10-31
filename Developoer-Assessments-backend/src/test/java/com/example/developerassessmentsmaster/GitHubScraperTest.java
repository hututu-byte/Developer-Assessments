package com.example.developerassessmentsmaster;

import com.example.developerassessmentsmaster.model.Developer;
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
            // 爬取开发者数据
            JSONArray developers = scraper.fetchDevelopers("java", 10);
            assertNotNull(developers);
            assertTrue(developers.length() > 0);

            for (int i = 0; i < developers.length(); i++) {
                JSONObject devJson = developers.getJSONObject(i);
                Developer developer = new Developer();
                developer.setGithubId(devJson.getLong("id"));
                developer.setGithubUsername(devJson.getString("login"));
                // 根据需要填充其他字段

                // 保存开发者到数据库
                developerService.addDeveloper(developer); // 确保调用的是 addDeveloper 方法

                // 爬取项目数据
                JSONArray projects = scraper.fetchProjects(devJson.getString("login"));
                assertNotNull(projects);

                for (int j = 0; j < projects.length(); j++) {
                    JSONObject projJson = projects.getJSONObject(j);
                    // 在此处根据项目模型创建项目对象并保存
                    // Project project = new Project();
                    // project.setGithubId(projJson.getLong("id"));
                    // project.setOwner(projJson.getString("owner"));
                    // project.setName(projJson.getString("name"));
                    // developerService.saveProject(project); // 保存项目到数据库
                }
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}

