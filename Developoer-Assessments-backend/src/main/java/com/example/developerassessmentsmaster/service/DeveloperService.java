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
            List<String> topLanguages = getTopThreeLanguagesByDeveloper(developer.getGithubId());

            // 创建 DeveloperInfo 对象并设置字段
            DeveloperInfo info = new DeveloperInfo();
            info.setGithubUsername(developer.getGithubUsername());
            info.setTalentRank(String.valueOf(developer.getTalentRank()));
            info.setBio(developer.getBio());
            info.setCountry(developer.getCountry());
            info.setGithubId(developer.getGithubId());
            info.setFollowing(developer.getFollowing());
            info.setFollowers(developer.getFollowers());
            info.setScore(developer.getScore());

            // 将前三种语言作为 mostCommonTag 返回
            info.setMostCommonTag(String.join(", ", topLanguages));  // 将前三种语言连接为一个字符串
            developerInfoList.add(info);
        }

        return developerInfoList;
    }


    /**
     * 获取开发者的详细信息，包括基本信息和项目相关信息
     *
     * @param githubId 开发者的 GitHub ID
     * @return DeveloperDetailInfo 对象，包含详细信息
     */
    public DeveloperDetailInfo getDeveloperDetailInfoByGithubId(Long githubId) {
        // 查询开发者基本信息
        Developer developer = developerMapper.getDeveloperByGithubId(githubId);

        // 如果开发者不存在，返回 null
        if (developer == null) {
            return null;
        }

        DeveloperDetailInfo developerInfo = new DeveloperDetailInfo();
        developerInfo.setGithubId(developer.getGithubId());
        developerInfo.setGithubUsername(developer.getGithubUsername());
        developerInfo.setFollowers(developer.getFollowers());
        developerInfo.setFollowing(developer.getFollowing());
        developerInfo.setCountry(developer.getCountry());
        developerInfo.setBio(developer.getBio());

        // 查询开发者的所有项目
        List<Project> projects = projectMapper.getProjectsByCreatorId(githubId);

        // 计算项目总数
        int totalProjects = (projects != null) ? projects.size() : 0;
        developerInfo.setTotalProjects(totalProjects);

        // 计算总星标数和总分叉数
        int totalStars = 0;
        int totalForks = 0;
        if (projects != null && !projects.isEmpty()) {
            totalStars = projects.stream()
                    .filter(p -> p.getStars() != null)
                    .mapToInt(Project::getStars)
                    .sum();

            totalForks = projects.stream()
                    .filter(p -> p.getForks() != null)
                    .mapToInt(Project::getForks)
                    .sum();
        }

        developerInfo.setTotalStars(totalStars);
        developerInfo.setTotalForks(totalForks);

        // 获取星标前五的项目，并过滤掉无效的语言
        List<ProjectDetailInfo> topProjects = new ArrayList<>();
        if (projects != null && !projects.isEmpty()) {
            topProjects = projects.stream()
                    .filter(p -> p.getName() != null && p.getLanguage() != null && !p.getLanguage().equalsIgnoreCase("null"))
                    .sorted(Comparator.comparingInt(Project::getStars).reversed())
                    .limit(5)
                    .map(p -> new ProjectDetailInfo(
                            p.getName(),
                            p.getDescription() != null ? p.getDescription().trim() : "",
                            p.getStars() != null ? p.getStars() : 0,
                            p.getLanguage().trim().equalsIgnoreCase("null") ? "Unknown" : capitalizeFirstLetter(p.getLanguage().trim())
                    ))
                    .collect(Collectors.toList());
        }

        developerInfo.setTopProjects(topProjects);

        return developerInfo;
    }

    /**
     * 将字符串的首字母大写
     *
     * @param str 输入字符串
     * @return 首字母大写后的字符串
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    // 根据 github_username、language 和 country 查询开发者
    public List<DeveloperInfo> searchDevelopersByUsernameAndLanguages(String githubUsername, List<String> languages, String country) {
        List<DeveloperInfo> developerInfoList = new ArrayList<>();

        // 根据条件查询开发者
        List<Developer> developers = developerMapper.findDevelopersByUsernameAndCountry(githubUsername, country);

        log.info("Found developers: {}", developers);

        for (Developer developer : developers) {
            // 获取该开发者的前三大语言，并确保没有 "null" 和空字符串
            List<String> topLanguages = getTopThreeLanguagesByDeveloper(developer.getGithubId());

            log.info("Developer ID: {}, Top Languages after initial filtering: {}", developer.getGithubId(), topLanguages);

            // 如果传入了语言参数，过滤开发者是否包含这些语言
            if (languages != null && !languages.isEmpty()) {
                log.info("Filtering with languages: {}", languages);
                log.info("Developer ID: {}, Developer's top languages: {}", developer.getGithubId(), topLanguages);

                // 将传入的语言列表也转为小写，确保大小写一致
                List<String> lowerCaseLanguages = languages.stream()
                        .filter(lang -> lang != null && !lang.trim().isEmpty() && !lang.equalsIgnoreCase("null"))
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

                // 将 topLanguages 转为小写
                List<String> lowerCaseTopLanguages = topLanguages.stream()
                        .filter(lang -> lang != null && !lang.trim().isEmpty() && !lang.equalsIgnoreCase("null"))
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

                log.info("Lower case languages: {}", lowerCaseLanguages);
                log.info("Lower case top languages: {}", lowerCaseTopLanguages);

                // 检查是否有匹配的语言
                boolean isValidDeveloper = lowerCaseTopLanguages.stream()
                        .anyMatch(lowerCaseLanguages::contains);

                if (!isValidDeveloper) {
                    log.info("Developer ID: {} does not match the language filters.", developer.getGithubId());
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
            info.setScore(developer.getScore());
            info.setFollowers(developer.getFollowers());
            info.setFollowing(developer.getFollowing());

            // 只取最多三种有效语言，并确保不包含 "null"
            String mostCommonTag = topLanguages.stream()
                    .filter(lang -> lang != null && !lang.trim().isEmpty() && !lang.equalsIgnoreCase("null"))
                    .limit(3)
                    .collect(Collectors.joining(", "));

            log.info("Developer ID: {}, Most Common Tag: {}", developer.getGithubId(), mostCommonTag);

            info.setMostCommonTag(mostCommonTag);  // 设置前三种语言
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
            if (language != null) {
                language = language.trim(); // 去除首尾空格
                if (!language.isEmpty() && !language.equalsIgnoreCase("null")) { // 过滤掉空字符串和 "null"
                    language = language.toLowerCase(); // 统一为小写
                    languageCount.put(language, languageCount.getOrDefault(language, 0) + 1);
                }
            }
        }

        List<String> topLanguages = languageCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // 按出现次数降序排序
                .limit(3) // 只取前三
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("Top languages for developer {}: {}", githubId, topLanguages);

        // 将语言名称首字母大写以保持一致性（可选）
        return topLanguages.stream()
                .map(lang -> lang.substring(0, 1).toUpperCase() + lang.substring(1))
                .collect(Collectors.toList());
    }
}
