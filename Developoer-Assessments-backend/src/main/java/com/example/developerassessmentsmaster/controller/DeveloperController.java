package com.example.developerassessmentsmaster.controller;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    @Autowired
    private DeveloperService developerService;

    @PostMapping
    public void addDeveloper(@RequestBody Developer developer) {
        developerService.addDeveloper(developer);
    }

    @GetMapping("/{username}/projects")
    public List<Project> getDeveloperProjects(@PathVariable String username) {
        return developerService.getDeveloperProjects(username);
    }


}
