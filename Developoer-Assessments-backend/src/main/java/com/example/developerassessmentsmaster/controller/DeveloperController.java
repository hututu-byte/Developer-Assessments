package com.example.developerassessmentsmaster.controller;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.DeveloperInfo;
import com.example.developerassessmentsmaster.model.Project;
import com.example.developerassessmentsmaster.model.Result;
import com.example.developerassessmentsmaster.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    // 根据 github_id 获取开发者的基本信息
    // 使用 @PathVariable 获取路径变量中的 githubId
    @GetMapping("/basic-info/{githubId}")
    public Result<DeveloperInfo> getDeveloperBasicInfo(@PathVariable Long githubId) {
        DeveloperInfo developerInfo = developerService.getDeveloperBasicInfoByGithubId(githubId);

        if (developerInfo != null) {
            return Result.success(developerInfo);
        } else {
            return Result.error(401,"Developer not found");
        }
    }

    // 根据 github_username、language 和 country 查询开发者
    @GetMapping("/search")
    public Result<List<DeveloperInfo>> searchDevelopersByFilters(
            @RequestParam(required = false) String githubUsername,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "desc") String sort) {  // 增加排序参数，默认为升序

        // 如果传入了多个 language，按逗号分隔
        String[] languages = null;
        if (language != null && !language.isEmpty()) {
            languages = language.split(",");
        }

        // 查询开发者并返回结果
        List<DeveloperInfo> developerInfoList = developerService.searchDevelopersByUsernameAndLanguages(githubUsername, languages, country);

        // 根据 sort 参数进行排序
        if ("desc".equalsIgnoreCase(sort)) {
            developerInfoList.sort(Comparator.comparingDouble(DeveloperInfo::getScore).reversed());  // 降序
        } else {
            developerInfoList.sort(Comparator.comparingDouble(DeveloperInfo::getScore));  // 升序
        }

        // 如果没有找到符合条件的开发者，返回失败信息
        if (developerInfoList.isEmpty()) {
            return Result.error(401, "No developers found with the given filters.");
        }

        return Result.success(developerInfoList);
    }



}
