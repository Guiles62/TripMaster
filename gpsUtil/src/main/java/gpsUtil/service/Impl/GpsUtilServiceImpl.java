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
import java.util.stream.IntStream;

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


    @Override
    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

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

    @Override
    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }

    @Override
    public VisitedLocation trackUserLocation(User user) {
        gpsUtil.location.VisitedLocation userVisitedLocation = gpsUtil.getUserLocation(user.getUserId());
        Location location = new Location(userVisitedLocation.location.latitude,userVisitedLocation.location.longitude);
        VisitedLocation visitedLocation = new VisitedLocation(userVisitedLocation.userId,location,userVisitedLocation.timeVisited);
        user.addToVisitedLocations(visitedLocation);
        return visitedLocation;
    }

    @Override
    public List<Attraction> getNearByAttractions(User user) {
        VisitedLocation visitedLocation = getUserLocation(user);
        List<Attraction> attractionList = getAllAttractions();
        List<Attraction> nearbyAttractions = new ArrayList<>();
        List<NearAttractions> nearAttractionsList = new ArrayList<>();
        Location location = visitedLocation.location;
        for (Attraction attraction : attractionList) {
            Double distance = getDistance(attraction, location);
            nearAttractionsList.add(new NearAttractions(attraction, distance));
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

    @Override
    public List<Attraction> getAllAttractions() {
        List<gpsUtil.location.Attraction> attractions = gpsUtil.getAttractions();
        List<Attraction> attractionList = new ArrayList<>();
        for (gpsUtil.location.Attraction attraction : attractions) {
            attractionList.add(new Attraction(attraction.attractionName,attraction.city,attraction.state,attraction.latitude,attraction.longitude));
        }
        return attractionList;
    }

    @Override
    public User getUser(UUID userId){
        User user = initializeInternalUser(userId);
        return user;
    }


    private User initializeInternalUser(UUID userId) {
        User user = new User(userId);
        generateUserLocationHistory(user);
        return user;
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
