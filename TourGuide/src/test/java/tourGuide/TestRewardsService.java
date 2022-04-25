package tourGuide;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRewardsService {

	@Autowired
	GpsUtilProxy gpsUtilProxy;
	@Autowired
	RewardsCentralProxy rewardsCentralProxy;
	@Autowired
	TripPricerProxy tripPricerProxy;

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = tourGuideService.getAttractions().get(0);
		Location location = new Location(attraction.latitude,attraction.longitude);
		List<VisitedLocation> visitedLocations = new ArrayList<>();
		UUID userId = user.getUserId();
		Date date = new Date();
		visitedLocations.add(new VisitedLocation(userId,location,date));
		user.setVisitedLocations(visitedLocations);
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 0);
	}
	
	 // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		User user = tourGuideService.getUser("internalUser0");
		InternalTestHelper.setInternalUserNumber(1);

		tourGuideService.getRewards(user);
		List<UserReward> userRewards = tourGuideService.getRewards(user);
		tourGuideService.tracker.stopTracking();

		assertEquals(tourGuideService.getNearByAttractions(user).size(), userRewards.size());
	}
	
}
