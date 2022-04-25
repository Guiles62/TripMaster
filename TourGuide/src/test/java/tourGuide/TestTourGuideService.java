package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.junit.Ignore;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.*;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTourGuideService {

	@Autowired
	private GpsUtilProxy gpsUtilProxy;
	@Autowired
	private RewardsCentralProxy rewardsCentralProxy;
	@Autowired
	private TripPricerProxy tripPricerProxy;


	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<VisitedLocation> userVisitedLocation = new ArrayList<>();
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		userVisitedLocation.add(visitedLocation);
		user.setVisitedLocations(userVisitedLocation);
		List<NearByAttractions> attractions = tourGuideService.getNearByAttractions(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, providers.size());
	}

	@Test
	public void getRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<UserReward> userRewards = tourGuideService.getRewards(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(userRewards.size(), user.getUserRewards().size());
	}

	@Test
	public void getAttractions() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		List<Attraction> attractions = tourGuideService.getAttractions();

		assertEquals(26, attractions.size());
	}

	@Test
	public void getAllCurrentLocation() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, tripPricerProxy, rewardsCentralProxy);

		List<VisitedLocation> visitedLocations = tourGuideService.getAllCurrentLocations();
		assertEquals(0, visitedLocations.size());
	}
}
