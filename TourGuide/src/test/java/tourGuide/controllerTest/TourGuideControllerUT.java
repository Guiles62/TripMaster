package tourGuide.controllerTest;

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
import tourGuide.controller.TourGuideController;
import tourGuide.model.*;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TourGuideControllerUT {

    private TourGuideController tourGuideController;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private TourGuideService tourGuideService;

    @InjectMocks
    private User user;

    @InjectMocks
    private Attraction attraction;

    @InjectMocks
    private Location location;

    @InjectMocks
    private Money money;

    @InjectMocks
    private Provider provider;

    @InjectMocks
    private VisitedLocation visitedLocation;

    @Before
    public void setup() {
        tourGuideController = new TourGuideController(tourGuideService);
        user = new User();
        visitedLocation.setLocation(location);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        user.setVisitedLocations(visitedLocations);
        visitedLocations.add(visitedLocation);
        when(tourGuideService.getUser("username")).thenReturn(user);
    }

    @Test
    public void getLocationTest() throws Exception {
        when(tourGuideService.getLocation(user)).thenReturn(location);
        mockMvc.perform(get("/getLocation?username=username")).andExpect(status().isOk());
    }

    @Test
    public void getNearByAttractionsTest() throws Exception {
        when(tourGuideService.getNearbyAttractions(user)).thenReturn(user.getAttractions());
        mockMvc.perform(get("/getNearbyAttractions?username=username")).andExpect(status().isOk());
    }

    @Test
    public void getRewardsTest() throws Exception {
        when(tourGuideService.getRewards(user)).thenReturn(user.getUserRewards());
        mockMvc.perform(get("/getRewards?username=username")).andExpect(status().isOk());
    }

    @Test
    public void getAllCurrentLocationsTest() throws Exception {
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        when(tourGuideService.getAllCurrentLocations()).thenReturn(visitedLocations);
        mockMvc.perform(get("/getAllCurrentLocations")).andExpect(status().isOk());
    }

    @Test
    public void getTripDeals() throws Exception {
        List<Provider> providers = new ArrayList<>();
        when(tourGuideService.getTripDeals(user)).thenReturn(providers);
        mockMvc.perform(get("/getTripDeals?username=username")).andExpect(status().isOk());
    }

}
