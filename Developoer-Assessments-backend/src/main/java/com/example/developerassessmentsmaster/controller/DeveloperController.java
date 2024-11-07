package com.example.developerassessmentsmaster.controller;

import com.example.developerassessmentsmaster.model.*;
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

    /**
     * 根据 github_id 获取开发者的详细信息
     *
     * @param githubId 开发者的 GitHub ID
     * @return Result<DeveloperDetailInfo> 对象，包含详细信息
     */
    @GetMapping("/basic-info/{githubId}")
    public Result<DeveloperDetailInfo> getDeveloperBasicInfo(@PathVariable Long githubId) {
        DeveloperDetailInfo developerDetailInfo = developerService.getDeveloperDetailInfoByGithubId(githubId);

        if (developerDetailInfo != null) {
            return Result.success(developerDetailInfo);
        } else {
            return Result.error(401, "Developer not found");
        }
    }

    // 根据 github_username、language 和 country 查询开发者
    @GetMapping("/search")
    public Result<List<DeveloperInfo>> searchDevelopersByFilters(
            @RequestParam(required = false) String githubUsername,
            @RequestParam(required = false) List<String> language, // 修改为 List<String>
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "desc") String sort) {  // 增加排序参数，默认为降序

        // 直接使用传入的语言列表，无需分割
        List<String> languages = language;

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
