package rewardsCentral.controllerUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rewardsCentral.controller.RewardsCentralController;
import rewardsCentral.model.User;
import rewardsCentral.service.Impl.RewardsCentralServiceImpl;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private RewardsCentralServiceImpl rewardsCentralService;

    @Before
    public void setup() {
        rewardsCentralController = new RewardsCentralController(rewardsCentralService);
        user = new User();
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
                        .content(asJsonString(new User()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }




}
