package tripPricer.controllerTest;

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
import tripPricer.controller.TripPricerController;
import tripPricer.model.User;
import tripPricer.service.TripPricerService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TripPricerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private User user;

    private TripPricerController tripPricerController;

    @MockBean
    private TripPricerService tripPricerService;

    @Before
    public void setup() {
        tripPricerController = new TripPricerController(tripPricerService);
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
    public void getPriceTest() throws Exception {
        mockMvc.perform(post("/getPrice?url=localhost:9090")
                        .content(asJsonString(new User()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
