package gpsUtil.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;

import java.util.List;


public interface GpsUtilService {

    boolean isWithinAttractionProximity(Attraction attraction, Location location);
    boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction);
    double getDistance(Location loc1, Location loc2);
    VisitedLocation getUserLocation(User user);
    VisitedLocation trackUserLocation(User user);
    List<Attraction> getNearAttractions(VisitedLocation visitedLocation);
    List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);
    List<Attraction> getAllAttractions();




}
