package com.example.developerassessmentsmaster.controller;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.DeveloperInfo;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.model.Result;
import com.example.developerassessmentsmaster.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {
    @Autowired
    private DeveloperService developerService;

    @GetMapping("/random")
    public Result<List<DeveloperInfo>> getRandomDevelopers() {
        List<DeveloperInfo> developers = developerService.getRandomDevelopersWithProjects();
        return Result.success(developers);
    }

}
