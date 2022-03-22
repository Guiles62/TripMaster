package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gpsUtil.location.Attraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;


@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	public Tracker tracker;
	boolean testMode = true;

	GpsUtilProxy gpsUtilProxy;
	TripPricerProxy tripPricerProxy;
	RewardsCentralProxy rewardsCentralProxy;
	
	public TourGuideService() {

		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	public TourGuideService(GpsUtilProxy gpsUtilProxy, TripPricerProxy tripPricerProxy, RewardsCentralProxy rewardsCentralProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.tripPricerProxy = tripPricerProxy;
		this.rewardsCentralProxy = rewardsCentralProxy;
	}

	public VisitedLocation getLocation (User user) {
		VisitedLocation visitedLocation = gpsUtilProxy.getLocation(user);
		return visitedLocation;
	}

	public List<Attraction> getNearbyAttractions (User user) {
		List<Attraction> nearAttractions = gpsUtilProxy.getNearbyAttractions(user);
		List<Attraction> nearFiveAttractions = new ArrayList<>();
		for (int i = 0; i <=4 ; i++) {
			nearFiveAttractions.add(nearAttractions.get(i));
		}
		return nearFiveAttractions;
	}

	public List<UserReward> getRewards (User user) {
		List<UserReward> getUserRewardsList = rewardsCentralProxy.getRewards(user);
		return getUserRewardsList;
	}

	public List<VisitedLocation>getAllCurrentLocations() {
		List<VisitedLocation> usersCurrentVisitedLocationList = new ArrayList<>();
		List<User> userList = getAllUsers();
		for(User user : userList){
			usersCurrentVisitedLocationList.add(gpsUtilProxy.getCurrentLocation(user));
		}
		return usersCurrentVisitedLocationList;
	}

	public List<Provider> getTripDeals(User user) {
		List<UserReward> userRewardList = rewardsCentralProxy.getRewards(user);
		int rewardsPoints = rewardsCentralProxy.getUserRewardsPointsSum(user, userRewardList);
		List<Provider> getPrice = tripPricerProxy.getPrice(user, tripPricerApiKey, rewardsPoints);
		return getPrice;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation userLocation = gpsUtilProxy.trackUserLocation(user);
		return userLocation;
	}

	public List<Attraction> getAttractions() {
		List<Attraction> attractionList = gpsUtilProxy.getAllAttractions();
		return attractionList;
	}

	/* a mettre dans RewardsCentralService ou à laisser ici ??
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}*/

	/* a mettre dans GpsUtilService
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}*/
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public String getApiKey() {
		return tripPricerApiKey;
	}

	/* a mettre dans TripPricerService ?? car tripPricerApiKey
	public List<Provider> getTripDeals(String userName, User user, String apiKey, int rewardsPoints) {
		rewardsPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), rewardsPoints);
		user.setTripDeals(providers);
		return providers;
	}*/

	/* a mettre dans GpsUtilService
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	} */

	/* a mettre dans GpsUtilService
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtil.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}
		
		return nearbyAttractions;
	} */

	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilProxy.getAllAttractions();
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction,user)));
					}
				}
			}
		}
	}

	public Boolean nearAttraction (VisitedLocation visitedLocation, Attraction attraction) {
		return gpsUtilProxy.nearAttraction(visitedLocation,attraction);
	}

	public int getRewardPoints(Attraction attraction, User user) {
		gpsUtilProxy.trackUserLocation(user);
		return rewardsCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	public Boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return gpsUtilProxy.isWithinAttractionProximity(attraction,location);
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
