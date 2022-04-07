package gpsUtil.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;

import java.util.List;
import java.util.UUID;


public interface GpsUtilService {

    User getUser(UUID userId);
    boolean isWithinAttractionProximity(Attraction attraction, Location location);
    boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction);
    double getDistance(Location loc1, Location loc2);
    VisitedLocation getUserLocation(UUID userId);
    VisitedLocation trackUserLocation(User user);
    List<Attraction> getNearByAttractions(User user);
    List<Attraction> getAllAttractions();
}
