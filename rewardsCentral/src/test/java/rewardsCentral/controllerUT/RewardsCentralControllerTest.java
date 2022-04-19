package rewardsCentral.controllerUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rewardsCentral.controller.RewardsCentralController;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.service.RewardsCentralService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RewardsCentralControllerTest {

    private RewardsCentralController rewardsCentralController;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private User user;

    @InjectMocks
    private UserReward userReward;

    @MockBean
    private RewardsCentralService rewardsCentralService;

    @Before
    public void setup() {
        rewardsCentralController = new RewardsCentralController(rewardsCentralService);
        List<UserReward> userRewards = new ArrayList<>();
        userReward = new UserReward();
        userRewards.add(userReward);
        user = new User();
        user.setUserRewards(userRewards);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getRewardsTest() throws Exception {
        mockMvc.perform(post("/getRewards")
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }




}
