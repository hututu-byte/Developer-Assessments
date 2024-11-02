package com.example.developerassessmentsmaster.repository;

import com.example.developerassessmentsmaster.model.Developer;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeveloperMapper {
    void insertDeveloper(Developer developer);

}
