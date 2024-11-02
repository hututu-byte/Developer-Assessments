package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {
    @Insert("INSERT INTO projects (github_id, creator_id, name, stars, forks, description, language, created_at, updated_at) " +
            "VALUES (#{githubId}, #{creatorId}, #{name}, #{stars}, #{forks}, #{description}, #{language}, #{createdAt}, #{updatedAt})")
    void insertProject(Project project);
}
