package tourGuide.service;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;



@Service
public class TourGuideService {

	private GpsUtilProxy gpsUtilProxy;
	private TripPricerProxy tripPricerProxy;
	private RewardsCentralProxy rewardsCentralProxy;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	public Tracker tracker;
	boolean testMode = true;



	public TourGuideService(GpsUtilProxy gpsUtilProxy, TripPricerProxy tripPricerProxy, RewardsCentralProxy rewardsCentralProxy) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.tripPricerProxy = tripPricerProxy;
		this.rewardsCentralProxy = rewardsCentralProxy;


		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();

	}

	public Location getLocation (User user) {
		Location location = gpsUtilProxy.getLocation(user.getUserId());
		return location;
	}

	public VisitedLocation trackUserLocation(User user) {
		return gpsUtilProxy.trackUserLocation(user.getUserId());
	}


	public List<Attraction> getNearbyAttractions (User user) {
		List<Attraction> nearAttractions = gpsUtilProxy.getNearbyAttractions(user.getUserId());
		return nearAttractions;
	}

	public List<UserReward> getRewards (User user) {
		List<UserReward> userRewards = rewardsCentralProxy.getRewards(user);
		return userRewards;
	}


	public List<VisitedLocation>getAllCurrentLocations() {
		List<VisitedLocation> usersCurrentVisitedLocationList = new ArrayList<>();
		List<User> userList = getAllUsers();
		for(User user : userList){
			usersCurrentVisitedLocationList.add(gpsUtilProxy.getCurrentLocation(user.getUserId()));
		}
		return usersCurrentVisitedLocationList;
	}

	public List<Provider> getTripDeals(User user) {
		String apiKey  = getApiKey();
		List<Provider> getPrice = tripPricerProxy.getPrice(user,apiKey);
		return getPrice;
	}

	public List<Attraction> getAttractions() {
		List<Attraction> attractionList = gpsUtilProxy.getAllAttractions();
		return attractionList;
	}
	
	public User getUser(String userName) {
		User user = internalUserMap.get(userName);
		List<VisitedLocation> visitedLocations = gpsUtilProxy.getUserVisitedLocation(user.getUserId());
		user.setVisitedLocations(visitedLocations);
		List<Attraction> attractions = getNearbyAttractions(user);
		user.setAttractions(attractions);
		List<UserReward> userRewards = rewardsCentralProxy.getRewards(user);
		user.setUserRewards(userRewards);
		return user;
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
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	
}
