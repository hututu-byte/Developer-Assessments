package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Developer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DeveloperMapper {
    void insertDeveloper(Developer developer);

    void updateDeveloperByGithubId(Developer developer);

    Developer getDeveloperByGithubId(Long githubId);

}
