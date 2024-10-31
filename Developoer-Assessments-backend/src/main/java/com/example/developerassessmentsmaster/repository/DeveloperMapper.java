package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Developer;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeveloperMapper {
    List<Developer> getAllDevelopers();
    void insertDeveloper(Developer developer);

}
