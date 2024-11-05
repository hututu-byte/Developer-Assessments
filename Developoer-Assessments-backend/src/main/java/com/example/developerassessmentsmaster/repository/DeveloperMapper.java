package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.LanguageCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DeveloperMapper {

    // 查询所有开发者
    List<Developer> findAllDevelopers();

    void insertDeveloper(Developer developer);

    void updateDeveloperByGithubId(Developer developer);

    Developer getDeveloperByGithubId(Long githubId);

    List<Developer> getRandomDevelopers();

    LanguageCount getMostCommonTagByDeveloper(@Param("githubId") Long githubId);

    List<Developer> findDevelopersByGithubUsernameAndCountry(@Param("githubUsername") String githubUsername,
                                                             @Param("country") String country);


    List<Developer> findDevelopersByGithubUsernameLanguageAndCountry(String githubUsername, String language, String country);

    List<Developer> findDevelopersByUsernameAndCountry(String githubUsername, String country);
}
