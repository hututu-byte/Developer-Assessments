package com.example.developerassessmentsmaster.utils;

import org.springframework.web.client.RestTemplate;


/**
 * 爬取指定用户数据
 */
public class GitHubApiUtils {
    private static final String GITHUB_API_URL = "https://api.github.com/users/";

    public static String fetchUserData(String username) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(GITHUB_API_URL + username, String.class);
    }
}
