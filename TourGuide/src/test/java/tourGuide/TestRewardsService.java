package tourGuide;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

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

public class TestRewardsService {

	GpsUtilProxy gpsUtilProxy;
	RewardsCentralProxy rewardsCentralProxy;
	TripPricerProxy tripPricerProxy;

	@Test
	@Ignore
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
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		Attraction attraction = tourGuideService.getAttractions().get(0);
		assertTrue(tourGuideService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);

		tourGuideService.getRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(tourGuideService.getAttractions().size(), userRewards.size());
	}
	
}
