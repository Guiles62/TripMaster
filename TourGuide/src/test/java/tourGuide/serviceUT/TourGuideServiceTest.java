package tourGuide.serviceUT;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.*;
import java.util.concurrent.ExecutionException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TourGuideServiceTest {


    GpsUtilProxy gpsUtilProxy = mock(GpsUtilProxy.class);


    RewardsCentralProxy rewardsCentralProxy = mock(RewardsCentralProxy.class);


    TripPricerProxy tripPricerProxy = mock(TripPricerProxy.class);

    User user = mock(User.class);


    private TourGuideService tourGuideService;

    private Location location;

    private VisitedLocation visitedLocation;

    private Provider provider;

    @Before
    public void setup() {
        tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        provider = new Provider(UUID.randomUUID(),"name",20);
        UUID userId = UUID.fromString("c01f2ef7-27fb-4990-b1b2-e5e7f3690973");
        user = new User();
        user.setUserId(userId);
        location = new Location(1.22,32.00);
        visitedLocation = new VisitedLocation();
        visitedLocation.setLocation(location);
        visitedLocation.setTimeVisited(new Date());
        visitedLocation.setUserId(userId);
        visitedLocations.add(visitedLocation);
        user.setVisitedLocations(visitedLocations);
        UserReward userReward = new UserReward();
        List<UserReward> userRewards = new ArrayList<>();
        userRewards.add(userReward);
        user.setUserRewards(userRewards);
        List<Provider> providers = new ArrayList<>();
        providers.add(provider);
        user.setTripDeals(providers);
    }

    @Test
    public void getAllCurrentLocationsTest() {
        assertTrue(tourGuideService.getAllCurrentLocations().size() == 100);
    }

    @Test
    public void getUserTest() {
        User user1 = tourGuideService.getUser("internalUser0");
        assertTrue(user1.getUserName().contains("internalUser0"));
    }


    @Test
    public void getLocationTest() {
        when(tourGuideService.trackUserLocation(user)).thenReturn(visitedLocation);
        when(tourGuideService.getLocation(user)).thenReturn(location);
        assertTrue(tourGuideService.getLocation(user) == user.getLastVisitedLocation().location);
    }

    @Test
    public void trackUserLocationTest() {
        when(tourGuideService.trackUserLocation(user)).thenReturn(visitedLocation);
        assertTrue(tourGuideService.trackUserLocation(user).location == user.getLastVisitedLocation().location);
    }

    @Test
    public void trackAllUsersLocation() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(tourGuideService.trackUserLocation(user)).thenReturn(visitedLocation);
        when(gpsUtilProxy.trackAllUsersLocation(users)).thenReturn(users);
        assertTrue(tourGuideService.trackAllUsersLocation(users).get(0).getLastVisitedLocation() == user.getLastVisitedLocation());
    }

    @Test
    public void getNearbyAttractionsTest() throws ExecutionException, InterruptedException {
        when(tourGuideService.trackUserLocation(user)).thenReturn(visitedLocation);
        List<Attraction> attractions = new ArrayList<>();
        Attraction attraction = new Attraction("name","city","state",1.2,13);
        Attraction attraction1 = new Attraction("name","city","state",1.2,13);
        Attraction attraction2 = new Attraction("name","city","state",1.2,13);
        Attraction attraction3 = new Attraction("name","city","state",1.2,13);
        Attraction attraction4 = new Attraction("name","city","state",1.2,13);
        attractions.add(attraction);
        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);
        when(gpsUtilProxy.getNearbyAttractions(user.getUserId())).thenReturn(attractions);
        assertTrue(tourGuideService.getNearByAttractions(user).size() == 5);
    }

    @Test
    public void getRewardsTest() {
        when(rewardsCentralProxy.getRewards(user)).thenReturn(user.getUserRewards());
        assertTrue(tourGuideService.getRewards(user) == user.getUserRewards());
    }

    @Test
    public void getAllRewardsTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(rewardsCentralProxy.getAllUsersRewards(users)).thenReturn(users);
        assertTrue(tourGuideService.getAllRewards(users).get(0).getUserRewards() == user.getUserRewards());
    }

    @Test
    public void getTripDealsTest() {
        Mockito.when(tripPricerProxy.getPrice(user,"test-server-api-key")).thenReturn(user.getTripDeals());
        assertEquals(tourGuideService.getTripDeals(user), user.getTripDeals());
    }

    @Test
    public void getAttractionsTest() {
        when(tourGuideService.trackUserLocation(user)).thenReturn(visitedLocation);
        List<Attraction> attractions = new ArrayList<>();
        Attraction attraction = new Attraction("name","city","state",1.2,13);
        Attraction attraction1 = new Attraction("name","city","state",1.2,13);
        Attraction attraction2 = new Attraction("name","city","state",1.2,13);
        Attraction attraction3 = new Attraction("name","city","state",1.2,13);
        Attraction attraction4 = new Attraction("name","city","state",1.2,13);
        attractions.add(attraction);
        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);
        when(gpsUtilProxy.getAllAttractions()).thenReturn(attractions);
        assertTrue(tourGuideService.getAttractions().size() == 5);
    }


}
