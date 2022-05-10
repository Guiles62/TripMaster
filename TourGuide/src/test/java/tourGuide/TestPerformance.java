package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.NearByAttractions;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPerformance {
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	@Autowired
	private GpsUtilProxy gpsUtilProxy;
	@Autowired
	private RewardsCentralProxy rewardsCentralProxy;
	@Autowired
	private TripPricerProxy tripPricerProxy;

	@Test
	public void highVolumeTrackLocation() {
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	

	@Test
	public void highVolumeGetRewards() throws ExecutionException, InterruptedException {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);

		List<User> allUsers = tourGuideService.getAllUsers();
		for (User users : allUsers) {
			List<VisitedLocation> visitedLocations = new ArrayList<>();
			visitedLocations.add(tourGuideService.trackUserLocation(users));
			users.setVisitedLocations(visitedLocations);
			List<NearByAttractions> nearByAttractions = tourGuideService.getNearByAttractions(users);
			List<Attraction> attractions = new ArrayList<>();
			for (NearByAttractions nearAttractions : nearByAttractions){
				attractions.add(nearAttractions.getAttraction());
			}
			users.setAttractions(attractions);
			List<UserReward> userRewards = tourGuideService.getRewards(users);
			users.setUserRewards(userRewards);
		}
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void rewardsTest() throws ExecutionException, InterruptedException {
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy,tripPricerProxy,rewardsCentralProxy);
		List<User> allUsers = tourGuideService.getAllUsers();
		List<Attraction> attractions = tourGuideService.getAttractions();
		List<Attraction> userAttraction = new ArrayList<>();
		userAttraction.add(attractions.get(0));
		List<VisitedLocation> visitedLocations = new ArrayList<>();
		Location location = new Location(1.22, 20);
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
		visitedLocations.add(visitedLocation);
		for (User users : allUsers) {
			users.setVisitedLocations(visitedLocations);
			users.setAttractions(userAttraction);
			List<UserReward> rewards = tourGuideService.getRewards(users);
			users.setUserRewards(rewards);
			assertTrue(users.getUserRewards().size() > 0);
		}

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}
