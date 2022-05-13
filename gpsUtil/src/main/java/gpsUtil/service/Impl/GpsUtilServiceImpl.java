package gpsUtil.service.Impl;

import gpsUtil.GpsUtil;
import gpsUtil.model.Attraction;
import gpsUtil.model.Location;
import gpsUtil.model.VisitedLocation;
import gpsUtil.model.NearAttractions;
import gpsUtil.model.User;
import gpsUtil.service.GpsUtilService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * <b>GpsUtilServiceImpl is the class which implement GpsUtilService and call GpsUtil.jar</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>nearAttraction</li>
 *         <li>getDistance</li>
 *         <li>getUserLocation</li>
 *         <li>trackUserLocation</li>
 *         <li>getNearByAttractions</li>
 *         <li>getAllAttractions</li>
 *         <li>getUser</li>
 *         <li>initializeInternalUser</li>
 *         <li>generateUserLocationHistory</li>
 *         <li>generateRandomLongitude</li>
 *         <li>generateRandomLatitude</li>
 *         <li>getRandomTime</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@Service
public class GpsUtilServiceImpl implements GpsUtilService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;


    private final GpsUtil gpsUtil;

    public GpsUtilServiceImpl() {
        gpsUtil = new GpsUtil();
    }

    public GpsUtilServiceImpl(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }


    /**
     * indicate if a distance between a user's visitedLocation and an attraction is greater than a proximityBuffer
     * @param visitedLocation the user's visitedLocation
     * @param attraction the attraction
     * @return a boolean that indicates if the distance is greater than proximityBuffer or not
     */
    @Override
    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    /**
     * calculate the distance between 2 points
     * @param loc1 is the first location
     * @param loc2 is the second location
     * @return a distance in nautical miles
     */
    @Override
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
     * get the user's last visitedLocation
     * @param user the user we use
     * @return the user's last visitedLocation
     */
    @Override
    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }

    /**
     * call gpsUtil jar to get the user location and add it in his visitedLocation
     * @param user the user we use
     * @return the user's visitedLocation
     */
    @Override
    public VisitedLocation trackUserLocation(User user) {
        gpsUtil.location.VisitedLocation userVisitedLocation = gpsUtil.getUserLocation(user.getUserId());
        Location location = new Location(userVisitedLocation.location.latitude,userVisitedLocation.location.longitude);
        VisitedLocation visitedLocation = new VisitedLocation(userVisitedLocation.userId,location,userVisitedLocation.timeVisited);
        user.addToVisitedLocations(visitedLocation);
        return visitedLocation;
    }

    /**
     * get closest 5 attractions to the user and sort them from closest to furthest
     * @param user the user we use
     * @return a list of attractions sort by distance to the user
     */
    @Override
    public List<Attraction> getNearByAttractions(User user) {
        ExecutorService executorService = Executors.newFixedThreadPool(2000);
        VisitedLocation visitedLocation = getUserLocation(user);
        List<Attraction> attractionList = getAllAttractions();
        List<Attraction> nearbyAttractions = new ArrayList<>();
        CopyOnWriteArrayList<NearAttractions> nearAttractionsList = new CopyOnWriteArrayList<>();
        Location location = visitedLocation.location;
        for (Attraction attraction : attractionList) {
            Double distance = getDistance(attraction, location);
            CompletableFuture.supplyAsync(() ->
            nearAttractionsList.add(new NearAttractions(attraction, distance)), executorService);
        }
        Collections.sort(nearAttractionsList, new Comparator<NearAttractions>() {
            @Override
            public int compare(NearAttractions attraction1, NearAttractions attraction2) {
                return attraction1.getDistanceInMilesBetweenUserAndAttraction().compareTo(attraction2.getDistanceInMilesBetweenUserAndAttraction());
            }
        });
        for (int i=0; i<5 ; i++) {
            Attraction attractionsList = nearAttractionsList.get(i).getAttraction();
            nearbyAttractions.add(attractionsList);
        }
        return nearbyAttractions;
    }

    /**
     * call gpsUtil jar to get the list of all attractions
     * @return a list of all attractions
     */
    @Override
    public List<Attraction> getAllAttractions() {
        ExecutorService executorService = Executors.newFixedThreadPool(2000);
        List<gpsUtil.location.Attraction> attractions = gpsUtil.getAttractions();
        CopyOnWriteArrayList<Attraction> attractionList = new CopyOnWriteArrayList<>();
        for (gpsUtil.location.Attraction attraction : attractions) {
            CompletableFuture.supplyAsync(() ->
            attractionList.add(new Attraction(attraction.attractionName,attraction.city,attraction.state,attraction.latitude,attraction.longitude)),executorService);
        }
        executorService.shutdown();
        return attractionList;
    }

    /**
     * call initializeInternalUser to get the user
     * @param userId the user's id
     * @return the user
     */
    @Override
    public User getUser(UUID userId){
        User user = initializeInternalUser(userId);
        return user;
    }


    /**
     * initialize an internal user
     * @param userId the user id
     * @return the user
     */
    private User initializeInternalUser(UUID userId) {
        User user = new User(userId);
        generateUserLocationHistory(user);
        return user;
    }

    /**
     * generate an user location history
     * @param user the user for whom we generate location history
     */
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i-> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    /**
     * generate a random longitude
     * @return a double longitude
     */
    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * generate a random latitude
     * @return a double latitude
     */
    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * generate a random date
     * @return a date
     */
    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
