package com.example.developerassessmentsmaster.service;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.repository.DeveloperMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeveloperService {
    @Autowired
    private DeveloperMapper developerMapper;

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
            developer.setName(devJson.optString("name")); // 注意处理可能缺失的字段
            developer.setEmail(devJson.optString("email"));
            developer.setCreatedAt(LocalDateTime.now());
            developer.setUpdatedAt(LocalDateTime.now());
            // 保存到数据库
            developerMapper.insertDeveloper(developer);
        }
    }


}
