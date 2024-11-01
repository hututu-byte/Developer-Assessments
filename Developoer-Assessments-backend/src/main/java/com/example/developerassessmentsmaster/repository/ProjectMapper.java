package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {
    @Insert("INSERT INTO Projects (github_id, name, stars, forks, description, created_at, updated_at) " +
            "VALUES (#{githubId}, #{name}, #{stars}, #{forks}, #{description}, #{createdAt}, #{updatedAt})")
    void insertProject(Project project);
}
