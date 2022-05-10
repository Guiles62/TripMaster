package tourGuide.service;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tourGuide.helper.InternalTestHelper;
import tourGuide.model.*;
import tourGuide.proxy.GpsUtilProxy;
import tourGuide.proxy.RewardsCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;


/**
 * <b>TourGuideService is the class that will call the different proxies of the other microservices to retrieve the data and process it</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getLocation</li>
 *         <li>trackUserLocation</li>
 *         <li>getNearbyAttractions</li>
 *         <li>getRewards</li>
 *         <li>getAllCurrentLocations</li>
 *         <li>getTripDeals</li>
 *         <li>getAttractions</li>
 *         <li>getUser</li>
 *         <li>getDistance</li>
 *         <li>getAllUsers</li>
 *         <li>addUser</li>
 *         <li>getApiKey</li>
 *         <li>addShutDownHook</li>
 *         <li>initializeInternalUsers</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@Service
public class TourGuideService extends Thread{

	private GpsUtilProxy gpsUtilProxy;
	private TripPricerProxy tripPricerProxy;
	private RewardsCentralProxy rewardsCentralProxy;

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	public Tracker tracker;
	boolean testMode = true;
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;



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

	public TourGuideService() {
	}

	/**
	 * call gpsUtilProxy to get user's location from gpsUtil microservice
	 * @param user user for whom we want the last localization
	 * @return the user's location
	 */
	public Location getLocation (User user) {
		Location location = gpsUtilProxy.getLocation(user.getUserId());
		return location;
	}

	/**
	 * call the gpsUtilProxy to get user's current location from gpsUtil microservice
	 * @param user user for whom we want the last visitedLocation
	 * @return the last visitedLocation
	 */
	public VisitedLocation trackUserLocation(User user) {
		return gpsUtilProxy.trackUserLocation(user.getUserId());
	}


	/**
	 * call the gpsProxy to get the closest 5 attractions to the user from gpsUtil microservice
	 * @param user user we use to find the 5 attractions near him
	 * @return a list of 5 attractions
	 */
	public List<NearByAttractions> getNearByAttractions (User user) {
		List<Attraction> nearAttractions = gpsUtilProxy.getNearbyAttractions(user.getUserId());
		List<NearByAttractions> nearByAttractions = new ArrayList<>();
		for (Attraction attraction : nearAttractions) {
			int rewardsPoints = rewardsCentralProxy.getAttractionRewardPoints(user.getUserId(),attraction.attractionId);
			Location location = new Location(attraction.latitude,attraction.longitude);
			double distance = getDistance(user.getLastVisitedLocation().location,location);
			nearByAttractions.add(new NearByAttractions(attraction,distance,rewardsPoints));
		}
		return nearByAttractions;
	}

	/**
	 * call the rewardsCentralProxy to get a list of rewards for a user from rewardCentral microservice
	 * @param user user we want to get his rewards
	 * @return a list of UserRewards
	 */
	public List<UserReward> getRewards (User user) throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100000);
		CompletableFuture<List<UserReward>> completableFuture = CompletableFuture.supplyAsync(() -> rewardsCentralProxy.getRewards(user), executorService);
		CompletableFuture<Void> future = completableFuture.thenAccept( s -> user.setUserRewards(s) );
		future.join();
		return user.getUserRewards();
	}


	/**
	 * call the gpsUtilProxy to get all the users locations from gpsUtil microservice
	 * @return a list of users visitedLocations
	 */
	public List<VisitedLocation>getAllCurrentLocations() {
		List<VisitedLocation> usersCurrentVisitedLocationList = new ArrayList<>();
		List<User> userList = getAllUsers();
		for(User user : userList){
			VisitedLocation currentLocation = gpsUtilProxy.getCurrentLocation(user.getUserId());
			usersCurrentVisitedLocationList.add(currentLocation);
		}
		return usersCurrentVisitedLocationList;
	}

	/**
	 * call the tripPricerProxy to get user tripDeals from tripPricer microservice
	 * @param user is the user we use to get his tripDeals
	 * @return a list of provider with prices
	 */
	public List<Provider> getTripDeals(User user) {
		String apiKey  = getApiKey();
		List<Provider> getPrice = tripPricerProxy.getPrice(user,apiKey);
		return getPrice;
	}

	/**
	 * call the gpsUtilProxy to get all the attractions from gpsUtil microservice
	 * @return a list of all Attractions
	 */
	public List<Attraction> getAttractions() {
		List<Attraction> attractionList = gpsUtilProxy.getAllAttractions();
		return attractionList;
	}

	/**
	 * this method calls the different microservices to retrieve the user
	 * @param userName username of the user
	 * @return the user
	 */
	public User getUser(String userName) {
		User user = internalUserMap.get(userName);
		List<VisitedLocation> visitedLocations = gpsUtilProxy.getUserVisitedLocation(user.getUserId());
		user.setVisitedLocations(visitedLocations);
		List<NearByAttractions> attractions = getNearByAttractions(user);
		List<Attraction> attractionList = new ArrayList<>();
		for (NearByAttractions attractions1 : attractions){
			attractionList.add(attractions1.getAttraction());
			user.setAttractions(attractionList);
		}

		List<UserReward> userRewards = rewardsCentralProxy.getRewards(user);
		user.setUserRewards(userRewards);
		return user;
	}

	/**
	 * calculate the distance between 2 points
	 * @param loc1 is the first location
	 * @param loc2 is the second location
	 * @return a distance in nautical miles
	 */
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	/**
	 * get all users
	 * @return a list of users
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * add user to list
	 * @param user the user to add
	 */
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * get the ApiKey
	 * @return a string of the tripPricerApiKey
	 */
	public String getApiKey() {
		return tripPricerApiKey;
	}

	/**
	 * shutdown tracker
	 */
	public void addShutDownHook() {
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
