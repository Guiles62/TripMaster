package gpsUtil.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
public class GpsUtilController {

    private GpsUtilService gpsUtilService;


    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @RequestMapping ("/getLocation")
    public Location getLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return visitedLocation.location;
    }

    @RequestMapping("/getUserVisitedLocation")
    public List<VisitedLocation> getUserVisitedLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        List<VisitedLocation> userVisitedLocations = user.getVisitedLocations();
        return userVisitedLocations;
    }

    @RequestMapping("/getNearbyAttractions")
    public List<Attraction> getNearbyAttractions(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        return gpsUtilService.getNearByAttractions(user);
    }
    @RequestMapping("/nearAttractions")
    public Boolean getNearAttraction(@RequestBody VisitedLocation visitedLocation, @RequestBody Attraction attraction) {
        Boolean nearAttraction = gpsUtilService.nearAttraction(visitedLocation, attraction);
        return nearAttraction;
    }

    @RequestMapping("/getCurrentLocation")
    public VisitedLocation getCurrentLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = user.getLastVisitedLocation();
        return visitedLocation;
    }

    @GetMapping(value = "/getAllAttractions")
    public List<Attraction> getAllAttractions(){
        List<Attraction>getAttractionsList = gpsUtilService.getAllAttractions();
        return getAttractionsList;
    }

    @GetMapping(value = "/trackUserLocation")
    public VisitedLocation trackUserLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.trackUserLocation(user);
        return visitedLocation;
    }


}
