package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper {
    List<Project> getProjectsByCreatorId(Long creatorId);

    void insertProject(Project project);

    List<String> getTopThreeLanguagesByDeveloper(Long githubId);


}
