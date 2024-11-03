package com.example.developerassessmentsmaster;

import com.example.developerassessmentsmaster.model.Developer;
import com.example.developerassessmentsmaster.model.TalentRank;
import com.example.developerassessmentsmaster.repository.DeveloperMapper;
import com.example.developerassessmentsmaster.service.DeveloperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DeveloperServiceTest {

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DeveloperMapper developerMapper;


    @Test
    public void testUpdateDeveloperTalentRank() {
        Long githubId = 35869981L; // 确保这个 ID 在数据库中存在
        // 确保开发者存在
        Developer developer = developerMapper.getDeveloperByGithubId(githubId);
        assertThat(developer).isNotNull();

        // 调用更新方法
        developerService.updateDeveloperTalentRank(githubId);

        // 再次查询开发者以验证更新
        Developer updatedDeveloper = developerMapper.getDeveloperByGithubId(githubId);
        assertThat(updatedDeveloper).isNotNull();
        assertThat(updatedDeveloper.getTalentRank()).isIn(TalentRank.LOW, TalentRank.MEDIUM, TalentRank.HIGH);
    }
}
