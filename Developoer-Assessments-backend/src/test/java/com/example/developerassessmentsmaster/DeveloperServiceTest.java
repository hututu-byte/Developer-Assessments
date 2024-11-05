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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DeveloperServiceTest {

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DeveloperMapper developerMapper;

    // 在测试开始前执行一次初始化，确保数据库中开发者数据存在
    @BeforeEach
    public void setUp() {
        // 在测试执行前，确保数据库中有开发者数据，避免空数据情况
        List<Developer> developers = developerMapper.findAllDevelopers();
        assertThat(developers).isNotEmpty();
    }

    @Test
    public void testUpdateAllDeveloperTalentRankAndScore() {
        // 获取所有开发者
        List<Developer> developers = developerMapper.findAllDevelopers();

        // 确保数据库中有开发者
        assertThat(developers).isNotEmpty();

        // 遍历每个开发者，更新其 talentRank 和 score
        for (Developer developer : developers) {
            developerService.updateDeveloperTalentRank(developer.getGithubId());

            // 查询更新后的开发者并验证其 talentRank 和 score
            Developer updatedDeveloper = developerMapper.getDeveloperByGithubId(developer.getGithubId());
            assertThat(updatedDeveloper).isNotNull();
            assertThat(updatedDeveloper.getTalentRank()).isIn(TalentRank.LOW, TalentRank.MEDIUM, TalentRank.HIGH);
            assertThat(updatedDeveloper.getScore()).isGreaterThanOrEqualTo(0);  // 验证 score 是否为非负数
        }
    }
}
