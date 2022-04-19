package gpsUtil.controllerUT;

import gpsUtil.controller.GpsUtilController;
import gpsUtil.model.Location;
import gpsUtil.model.User;
import gpsUtil.model.VisitedLocation;
import gpsUtil.service.GpsUtilService;
import gpsUtil.service.Impl.GpsUtilServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GpsUtilControllerTest {

    private GpsUtilController gpsUtilController;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private User user;

    @InjectMocks
    private VisitedLocation visitedLocation;

    @InjectMocks
    private Location location;

    @MockBean
    private GpsUtilService gpsUtilService;


    @Before
    public void setup() {
        gpsUtilController = new GpsUtilController(gpsUtilService);
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User(userId);
        visitedLocation.setLocation(location);
        user.addToVisitedLocations(visitedLocation);
        List<VisitedLocation>visitedLocations = new ArrayList<>();
        visitedLocations.add(visitedLocation);
        when(gpsUtilService.getUser(userId)).thenReturn(user);
    }

    @Test
    public void getLocationTest() throws Exception {
        when(gpsUtilService.getUserLocation(user)).thenReturn(visitedLocation);
        mockMvc.perform(post("/getLocation?userId=c01f2ef7-27fb-4990-b1b2-e5e7f3690973")).andExpect(status().isOk());
    }

    @Test
    public void getUserVisitedLocationTest() throws Exception {
        mockMvc.perform(post("/getUserVisitedLocation?userId=c01f2ef7-27fb-4990-b1b2-e5e7f3690973")).andExpect(status().isOk());
    }

    @Test
    public void getNearByAttractionsTest() throws Exception {
        mockMvc.perform(post("/getNearbyAttractions?userId=c01f2ef7-27fb-4990-b1b2-e5e7f3690973")).andExpect(status().isOk());
    }

    @Test
    public void getCurrentLocationTest() throws Exception {
        mockMvc.perform(post("/getCurrentLocation?userId=c01f2ef7-27fb-4990-b1b2-e5e7f3690973")).andExpect(status().isOk());
    }

    @Test
    public void getAllAttractionsTest() throws Exception {
        mockMvc.perform(get("/getAllAttractions")).andExpect(status().isOk());
    }

    @Test
    public void trackUserLocation() throws Exception {
        mockMvc.perform(post("/trackUserLocation?userId=c01f2ef7-27fb-4990-b1b2-e5e7f3690973")).andExpect(status().isOk());
    }
}
