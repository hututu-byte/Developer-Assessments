package com.example.developerassessmentsmaster.scraper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GitHubScraper {

    private final OkHttpClient client = new OkHttpClient();

    // 获取开发者的公共信息
    public JSONObject fetchDeveloperDetails(String username) throws Exception {
        String url = "https://api.github.com/users/" + username;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Failed to fetch developer details: " + response.code());
                throw new IOException("Unexpected code " + response);
            }
            return new JSONObject(response.body().string());
        }
    }


    // 根据邮箱域名推测国家
    public String getCountryFromEmail(String email) {
        if (email != null) {
            String domain = email.split("@")[1];
            if (domain.endsWith(".de")) {
                return "Germany";
            } else if (domain.endsWith(".uk")) {
                return "United Kingdom";
            } else if (domain.endsWith(".cn")) {
                return "China";
            } else if (domain.endsWith(".fr")) {
                return "France";
            }
            // 可以继续添加更多的域名规则
        }
        return "Unknown";
    }

    // 根据语言推测国家
    public String guessCountryBasedOnLanguage(List<String> languages) {
        for (String language : languages) {
            if ("zh-CN".equals(language) || "zh-TW".equals(language)) {
                return "China"; // 或者 "Taiwan"
            } else if ("fr".equals(language)) {
                return "France";
            } else if ("de".equals(language)) {
                return "Germany";
            }
            // 继续添加其他语言的判断
        }
        return "Unknown";
    }

    // 根据时区推测国家
    public String guessCountryFromTimezone(String timezone) {
        if ("Asia/Shanghai".equals(timezone)) {
            return "China";
        } else if ("Europe/Berlin".equals(timezone)) {
            return "Germany";
        }
        // 添加其他时区的规则
        return "Unknown";
    }

    // 根据开发者信息推测国家
    public String guessCountry(JSONObject developerDetails, List<String> languages) {
        // 从开发者详情中获取邮箱、时区等信息
        String email = developerDetails.optString("email");
        String timezone = developerDetails.optString("timezone");

        // 先通过邮箱推测
        String country = getCountryFromEmail(email);
        if (!"Unknown".equals(country)) {
            return country; // 如果邮箱有信息，直接返回
        }

        // 如果邮箱信息不全，尝试通过语言推测
        country = guessCountryBasedOnLanguage(languages);
        if (!"Unknown".equals(country)) {
            return country; // 如果语言有信息，返回推测的国家
        }

        // 如果邮箱和语言都没有信息，尝试通过时区推测
        country = guessCountryFromTimezone(timezone);
        return country;
    }


    // 爬取开发者列表
    public JSONArray fetchDevelopers(String query, int limit) throws Exception {
        String url = "https://api.github.com/search/users?q=" + query + "&per_page=" + limit + "&page=1";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONArray("items");
        }
    }

    // 获取开发者的所有项目
    public JSONArray fetchProjects(String username) throws Exception {
        String url = "https://api.github.com/users/" + username + "/repos";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return new JSONArray(responseBody);
        }
    }
}
