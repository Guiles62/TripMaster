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
import tourGuide.tracker.controller.TourGuideController;
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
        List<Attraction> attractions = new ArrayList<>();
        user = new User();
        user.setUserName("username");
        attraction = new Attraction("name","city","state",1.2,1.1);
        attractions.add(attraction);
        visitedLocation.setLocation(location);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        visitedLocations.add(visitedLocation);
        user.setVisitedLocations(visitedLocations);
        user.setAttractions(attractions);
        when(tourGuideService.getUser("username")).thenReturn(user);
    }

    @Test
    public void getLocationTest() throws Exception {
        when(tourGuideService.getLocation(user)).thenReturn(location);
        mockMvc.perform(get("/getLocation?userName=username")).andExpect(status().isOk());
    }

    @Test
    public void getNearByAttractionsTest() throws Exception {
        List<NearByAttractions> attractions = new ArrayList<>();
        NearByAttractions attraction1 = new NearByAttractions(attraction,100,50);
        NearByAttractions attraction2 = new NearByAttractions(attraction,50,50);
        NearByAttractions attraction3 = new NearByAttractions(attraction,50,50);
        NearByAttractions attraction4 = new NearByAttractions(attraction,50,50);
        NearByAttractions attraction5 = new NearByAttractions(attraction,50,50);
        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);
        attractions.add(attraction5);
        when(tourGuideService.getNearByAttractions(user)).thenReturn(attractions);
        mockMvc.perform(get("/getNearByAttractions?userName=username")).andExpect(status().isOk());
    }

    @Test
    public void getRewardsTest() throws Exception {
        when(tourGuideService.getRewards(user)).thenReturn(user.getUserRewards());
        mockMvc.perform(get("/getRewards?userName=username")).andExpect(status().isOk());
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
        mockMvc.perform(get("/getTripDeals?userName=username")).andExpect(status().isOk());
    }

}
