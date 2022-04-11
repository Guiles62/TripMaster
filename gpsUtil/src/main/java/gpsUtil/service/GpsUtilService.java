package gpsUtil.service;

import gpsUtil.model.Attraction;
import gpsUtil.model.Location;
import gpsUtil.model.VisitedLocation;
import gpsUtil.model.User;

import java.util.List;
import java.util.UUID;


public interface GpsUtilService {

    User getUser(UUID userId);
    boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction);
    double getDistance(Location loc1, Location loc2);
    VisitedLocation getUserLocation(User user);
    VisitedLocation trackUserLocation(User user);
    List<Attraction> getNearByAttractions(User user);
    List<Attraction> getAllAttractions();
}
