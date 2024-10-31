package com.example.developerassessmentsmaster.controller;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    @Autowired
    private DeveloperService developerService;

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developerService.getDevelopers();
    }

    @PostMapping
    public void addDeveloper(@RequestBody Developer developer) {
        developerService.addDeveloper(developer);
    }
}
