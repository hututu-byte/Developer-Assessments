package com.example.developerassessmentsmaster.scraper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubScraper {
    private final OkHttpClient client = new OkHttpClient();

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

    public JSONArray fetchProjects(String username) throws Exception {
        String url = "https://api.github.com/users/" + username + "/repos";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return new JSONArray(response.body().string());
        }
    }
}
