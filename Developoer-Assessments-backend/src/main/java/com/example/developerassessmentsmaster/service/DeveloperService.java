package com.example.developerassessmentsmaster.service;

import com.example.developerassessmentsmaster.model.*;
import com.example.developerassessmentsmaster.repository.DeveloperMapper;
import com.example.developerassessmentsmaster.repository.ProjectMapper;
import com.example.developerassessmentsmaster.scraper.GitHubScraper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        double score = calculateScore(developer, totalStars, totalForks);

        developer.setScore(score); // 假设你已经在 Developer 类中添加了 score 字段

        developerMapper.updateDeveloperByGithubId(developer);
    }
    private double calculateScore(Developer developer, int totalStars, int totalForks) {
        // 你可以根据需要调整这些权重
        return (developer.getFollowers() * 0.4) + (totalStars * 0.4) + (totalForks * 0.2);
    }


    private TalentRank calculateTalentRank(Developer developer, int totalStars, int totalForks) {
        double score = (developer.getFollowers() * 0.4) + (totalStars * 0.4) + (totalForks * 0.2);
        if (score < 200) {
            return TalentRank.LOW;
        } else if (score < 500) {
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




    public List<DeveloperInfo> getRandomDevelopersWithProjects() {
        // 获取随机的开发者列表
        List<Developer> developers = developerMapper.getRandomDevelopers();
        List<DeveloperInfo> developerInfoList = new ArrayList<>();

        for (Developer developer : developers) {
            // 获取开发者参与项目的前三种语言
            List<String> topLanguages = getTopThreeProjectLanguages(developer.getGithubId());

            // 创建 DeveloperInfo 对象并设置字段
            DeveloperInfo info = new DeveloperInfo();
            info.setGithubUsername(developer.getGithubUsername());
            info.setTalentRank(String.valueOf(developer.getTalentRank()));
            info.setBio(developer.getBio());
            info.setCountry(developer.getCountry());
            info.setGithubId(developer.getGithubId());

            // 将前三种语言作为 mostCommonTag 返回
            info.setMostCommonTag(String.join(", ", topLanguages));  // 将前三种语言连接为一个字符串
            developerInfoList.add(info);
        }

        return developerInfoList;
    }

    // 获取开发者参与项目的前三种语言
    private List<String> getTopThreeProjectLanguages(Long developerId) {
        // 获取该开发者的所有项目
        List<Project> projects = projectMapper.getProjectsByCreatorId(developerId);

        // 使用 Map 来统计语言的出现次数
        Map<String, Integer> languageCount = new HashMap<>();
        for (Project project : projects) {
            String language = project.getLanguage();  // 使用语言作为标签
            languageCount.put(language, languageCount.getOrDefault(language, 0) + 1);
        }

        // 按照出现次数降序排序，获取前三种语言
        return languageCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())  // 按值降序排序
                .limit(3)  // 获取前三种语言
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    // 根据 github_id 获取开发者的基本信息
    public DeveloperInfo getDeveloperBasicInfoByGithubId(Long githubId) {
        // 调用 mapper 查询开发者基本信息
        Developer developer = developerMapper.getDeveloperByGithubId(githubId);

        // 将查询到的 Developer 转换为 DeveloperInfo
        if (developer != null) {
            DeveloperInfo developerInfo = new DeveloperInfo();
            developerInfo.setGithubUsername(developer.getGithubUsername());
            developerInfo.setFollowers(developer.getFollowers());
            developerInfo.setFollowing(developer.getFollowing());
            developerInfo.setCountry(developer.getCountry());
            developerInfo.setBio(developer.getBio());
            return developerInfo;
        } else {
            // 如果没有找到该开发者，返回 null 或者抛出异常
            return null;
        }
    }

    // 获取开发者的最常见标签
    public LanguageCount getMostCommonTag(Long githubId) {
        return developerMapper.getMostCommonTagByDeveloper(githubId);
    }

    // 根据 github_username 和国家获取开发者
    public List<Developer> getDevelopersByUsernameAndCountry(String githubUsername, String country) {
        return developerMapper.findDevelopersByGithubUsernameAndCountry(githubUsername, country);
    }

    // 根据 github_username、language 和 country 查询开发者
    public List<DeveloperInfo> searchDevelopersByUsernameAndLanguages(String githubUsername, String[] languages, String country) {
        List<DeveloperInfo> developerInfoList = new ArrayList<>();

        // 根据条件查询开发者
        List<Developer> developers = developerMapper.findDevelopersByUsernameAndCountry(githubUsername, country);

        log.info("developers：{}",developers);

        for (Developer developer : developers) {
            // 获取该开发者的前三大语言
            List<String> topLanguages = getTopThreeLanguagesByDeveloper(developer.getGithubId());

            log.info("language：{}",topLanguages);

            // 如果传入了语言参数，过滤开发者是否包含这些语言
            if (languages != null && languages.length > 0) {
                // 输出日志，查看传入的 languages 和 topLanguages
                log.info("Filtering developers with languages: {}", Arrays.toString(languages));
                log.info("Developer's top languages: {}", topLanguages);

                // 过滤掉 topLanguages 中的 null 和空字符串
                List<String> validLanguages = topLanguages.stream()
                        .filter(language -> language != null && !language.trim().isEmpty())  // 过滤 null 和空字符串
                        .map(String::toLowerCase)  // 转小写
                        .collect(Collectors.toList());

                // 如果没有有效的语言，跳过该开发者
                if (validLanguages.isEmpty()) {
                    continue;
                }

                // 使用统一的小写格式来避免大小写不一致的问题
                boolean isValidDeveloper = validLanguages.stream()
                        .anyMatch(language -> Arrays.stream(languages)  // 避免使用 Arrays.asList, 直接用 stream
                                .map(String::toLowerCase)  // 转小写
                                .anyMatch(l -> l.equals(language)) // 进行匹配
                        );

                // 如果开发者的语言不匹配传入的语言，则跳过该开发者
                if (!isValidDeveloper) {
                    continue;
                }
            }

            // 创建 DeveloperInfo 并设置相关信息
            DeveloperInfo info = new DeveloperInfo();
            info.setScore(developer.getScore());
            info.setGithubId(developer.getGithubId());
            info.setGithubUsername(developer.getGithubUsername());
            info.setTalentRank(String.valueOf(developer.getTalentRank()));
            info.setBio(developer.getBio());
            info.setCountry(developer.getCountry());
            // 过滤掉 null 或空的语言
            List<String> validLanguages = topLanguages.stream()
                    .filter(language -> language != null && !language.isEmpty())
                    .collect(Collectors.toList());
            info.setMostCommonTag(String.join(", ", validLanguages));  // 设置前三种语言
            developerInfoList.add(info);
        }

        return developerInfoList;
    }

    public List<String> getTopThreeLanguagesByDeveloper(Long githubId) {
        List<Project> projects = projectMapper.getProjectsByCreatorId(githubId);

        if (projects == null || projects.isEmpty()) {
            log.info("No projects found for developer {}", githubId);
            return new ArrayList<>(); // 如果没有项目，返回空列表
        }

        // 统计语言出现频率
        Map<String, Integer> languageCount = new HashMap<>();
        for (Project project : projects) {
            String language = project.getLanguage();
            if (language != null && !language.isEmpty()) { // 过滤掉 null 或空字符串的语言
                languageCount.put(language, languageCount.getOrDefault(language, 0) + 1);
            }
        }

        List<String> topLanguages = languageCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue()) // 按出现次数降序排序
                .limit(3) // 只取前三
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        log.info("Top languages for developer {}: {}", githubId, topLanguages);

        return topLanguages;
    }




}
