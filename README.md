# GitHub 开发者评价应用开发文档

## 1. 项目概述
- **项目名称**：GitHub 开发者评价应用
- **项目简介**：基于 GitHub 的开源项目数据，对开发者的技术能力、所属地区和领域进行评估与排名。
- **目标用户**：开源项目维护者、招聘人员、技术社区管理者等。
- **开发背景与动机**：能够使开发者更好的寻找高级的开发者学习其技术，对于招聘人员可以根据应聘者的得分评估其编程水平。

## 2. 功能概述
### 2.1 基础功能（必需实现）
- **技术能力评估 (TalentRank)**：使用类似于 Google 搜索 PageRank 算法的方法，对开发者进行技术能力评估。说明如何根据项目的贡献度和重要性进行评价。
- **开发者的 Nation 检测**：描述系统如何根据开发者 Profile 和其相关网络痕迹预测其所属国家/地区。
- **开发者领域匹配**：通过分析项目标签和描述，匹配开发者领域并进行 TalentRank 排序。说明如何实现按地域筛选开发者的功能。

### 2.2 高级功能（可选实现）
- **数据置信度显示**：在用户界面上展示每次评估的置信度，处理数据不全或存在误差时显示 N/A 标签。
- **评估结果的自动整理**：说明如何自动整理和排序评估数据，并输出结构化的报告。
- **用户权限和隐私设置**：如何实现对用户数据隐私和访问权限的管理。

## 3. 系统架构
### 3.1 总体架构设计
- 系统架构图：展示安卓客户端和 Java 后端的交互架构
- 前后端通信方式：通过在Android Okhttp/Retrofit 与后端建立链接，从后端Api获取数据
- 技术栈说明：
- 前端：
  - UI：JetPack Compose。
  - 网络请求：Okhttp/Retrofit。
  - 依赖注入：Dagger/Hilt。
  - 异步：Kotlin Coroutines/Flow。
  - 页面导航：Compose Navigation。

### 3.2 数据处理流程
- **数据收集**：如何从 GitHub API 获取和整理开源项目数据。
- **数据清洗与分析**：描述如何处理和分析获取的数据，进行去噪和筛选。
- **算法实现**：详细讲解 TalentRank 算法的实现步骤。

## 4. 前端与后端设计
### 4.1 前端设计（安卓端）
- **使用的前端技术**：Android SDK、Kotlin，JetPack Compose，Flow，Coroutines，Hilt。
- **架构**：采用MVI的架构，视图与数据分离，数据，意图（intent）都是单向流动，解耦。
- **网络**：采用Okhttp以及Retrofit，简化网络请求
- **用户界面设计**：
![bdb448e1016ff24420e41297c703064d](https://github.com/user-attachments/assets/af7605a0-0547-4566-b27a-448bdf36f18f)
![ff356c00abf9c3b092370642e7c59d53](https://github.com/user-attachments/assets/d55a55a1-02ba-47cb-8a9e-169ee46a75a4)
![9ff3138edf524c55b7ef44265578fdff](https://github.com/user-attachments/assets/a25635a1-8ebd-4f0e-b560-acd495681ab3)
![6c582efc9c09d2a3616410d15ccae07e](https://github.com/user-attachments/assets/a78f7650-dd7c-46fb-8f70-031265022c7d)
![d52a5edf1d1dde37507e28c4092daf49](https://github.com/user-attachments/assets/3f4af879-eb0f-4378-95ec-98a5cd3ee1b4)
![ab422e886e9bcc59a10c1086b83b785a](https://github.com/user-attachments/assets/89d66e24-e74e-4b8c-abe8-dcf46a14fb60)
适配深色模式
![a8719de659c5d43665c095f1fc84f201](https://github.com/user-attachments/assets/45423ceb-783a-4f90-a413-26d36c463073)
![b2b32ff352eba8e16b9baa9a54eefea4](https://github.com/user-attachments/assets/795df3f3-b41d-432a-9aed-9bd0006c593d)
![36150f22447e6b6826a112d5531e5ca2](https://github.com/user-attachments/assets/07bfd601-4599-4bcc-97b9-b3d7f3959128)
![36150f22447e6b6826a112d5531e5ca2](https://github.com/user-attachments/assets/caab9485-f1e8-4518-bf58-7e6f692f5512)
- **数据展示与交互**：主页展示随机用户的数据以及TalentRank评分等级等，可以点击进去查看详细数据。搜索页面可以根据国家以及语言限制，对用户进行搜索，搜索出来的结果按照降序排列。
### 4.2 后端设计（Java）

- **API 设计与实现**：详细说明安卓前端与 Java 后端之间的 API 端点设计，包括请求和响应格式。

  后端API是前端与后端之间的桥梁，负责处理客户端请求，执行业务逻辑，并返回相应的数据。以下将详细介绍**开发者评估应用**的主要API接口，包括其功能、请求方式、路径、参数、响应格式及示例。

  #####  API 功能概述

  **开发者评估应用**提供以下主要API接口：

  1. **获取随机开发者列表**
  2. **获取开发者的详细信息**
  3. **根据过滤条件搜索开发者**

  #####  接口详细设计

  以下是三个主要API接口的详细设计：

  ------

  ##### **1. 获取随机开发者列表**

  - **接口路径**: `/api/developers/random`

  - **请求方法**: `GET`

  - **功能描述**: 获取一组随机的开发者信息，包括其TalentRank、国家、领域等基本信息。

  - **请求参数**: 无

  - **响应格式**:

    ```json
    json复制代码{
      "status": "success",
      "data": [
        {
          "githubId": 123456,
          "githubUsername": "johnDoe",
          "talentRank": "HIGH",
          "bio": "Senior Java Developer",
          "country": "China",
          "following": 50,
          "followers": 200,
          "score": 350.0,
          "mostCommonTag": "Java, Spring, MySQL"
        },
        ...
      ]
    }
    ```

  - **错误响应**:

    ```json
    json复制代码{
      "status": "error",
      "code": 500,
      "message": "Internal Server Error"
    }
    ```

  - **示例代码**:

    ```java
    java复制代码@GetMapping("/random")
    public Result<List<DeveloperInfo>> getRandomDevelopers() {
        List<DeveloperInfo> developers = developerService.getRandomDevelopersWithProjects();
        return Result.success(developers);
    }
    ```

  ------

  ##### **2. 获取开发者的详细信息**

  - **接口路径**: `/api/developers/basic-info/{githubId}`

  - **请求方法**: `GET`

  - **功能描述**: 根据开发者的GitHub ID获取其详细信息，包括关注者数、关注数、国家、Bio、项目列表等。

  - **请求参数**:

    - 路径参数

      :

      - `githubId` (Long): 开发者的GitHub ID

  - **响应格式**:

    ```json
    json复制代码{
      "status": "success",
      "data": {
        "githubId": 123456,
        "githubUsername": "johnDoe",
        "followers": 200,
        "following": 50,
        "country": "China",
        "bio": "Senior Java Developer",
        "totalProjects": 10,
        "totalStars": 500,
        "totalForks": 100,
        "topProjects": [
          {
            "name": "ProjectA",
            "description": "A Java-based project",
            "stars": 150,
            "language": "Java"
          },
          ...
        ]
      }
    }
    ```

  - **错误响应**:

    ```json
    json复制代码{
      "status": "error",
      "code": 401,
      "message": "Developer not found"
    }
    ```

  - **示例代码**:

    ```java
    java复制代码@GetMapping("/basic-info/{githubId}")
    public Result<DeveloperDetailInfo> getDeveloperBasicInfo(@PathVariable Long githubId) {
        DeveloperDetailInfo developerDetailInfo = developerService.getDeveloperDetailInfoByGithubId(githubId);
    
        if (developerDetailInfo != null) {
            return Result.success(developerDetailInfo);
        } else {
            return Result.error(401, "Developer not found");
        }
    }
    ```

  ------

  ##### **3. 根据过滤条件搜索开发者**

  - **接口路径**: `/api/developers/search`

  - **请求方法**: `GET`

  - **功能描述**: 根据GitHub用户名、编程语言和国家进行开发者搜索，支持按分数排序。

  - **请求参数**:

    - 查询参数

      :

      - `githubUsername` (String, 可选): 开发者的GitHub用户名
      - `language` (List<String>, 可选): 开发者擅长的编程语言列表
      - `country` (String, 可选): 开发者所属国家
      - `sort` (String, 可选, 默认值为 `desc`): 排序方式，`desc`为降序，`asc`为升序

  - **响应格式**:

    ```json
    json复制代码{
      "status": "success",
      "data": [
        {
          "githubId": 123456,
          "githubUsername": "johnDoe",
          "talentRank": "HIGH",
          "bio": "Senior Java Developer",
          "country": "China",
          "following": 50,
          "followers": 200,
          "score": 350.0,
          "mostCommonTag": "Java, Spring, MySQL"
        },
        ...
      ]
    }
    ```

  - **错误响应**:

    ```json
    json复制代码{
      "status": "error",
      "code": 401,
      "message": "No developers found with the given filters."
    }
    ```

  - **示例代码**:

    ```java
    java复制代码@GetMapping("/search")
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
    ```

  ------

  ##### 接口规范总结

  | 接口名称               | 方法 | 路径                                    | 请求参数                                                  | 响应数据类型          | 描述                      |
  | ---------------------- | ---- | --------------------------------------- | --------------------------------------------------------- | --------------------- | ------------------------- |
  | 获取随机开发者列表     | GET  | `/api/developers/random`                | 无                                                        | `List<DeveloperInfo>` | 获取随机开发者信息        |
  | 获取开发者详细信息     | GET  | `/api/developers/basic-info/{githubId}` | 路径参数: `githubId` (Long)                               | `DeveloperDetailInfo` | 根据GitHub ID获取详细信息 |
  | 根据过滤条件搜索开发者 | GET  | `/api/developers/search`                | 查询参数: `githubUsername`, `language`, `country`, `sort` | `List<DeveloperInfo>` | 根据条件搜索开发者        |

- **数据库设计**：数据库表结构与存储方案（例如，用户信息表、开发者评估表）。

## 5. 部署与发布

- **本地部署** 目前还在本地部署测试阶段

## 6. 用户指南

- 用户使用项目见 : 项目程序运行说明.md。

## 7. 未来改进计划

- 获取用户的头像，优化页面
- 对于搜索采用分页的方法，优化搜索策略，使搜索速度更快
- 优化评分算法，以及国家预测算法，使结果更精确
- 用户反馈的整合与应用迭代计划

## 8. 附录

- Android App中大部分图标来自阿里巴巴IconFont开源图标库，用于非盈利目的，侵权必删。
