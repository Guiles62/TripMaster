package gpsUtil.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class GpsUtilController {

    private GpsUtilService gpsUtilService;


    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @RequestMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) {
        User user = gpsUtilService.getUser(userName);
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return visitedLocation.location;
    }

    @RequestMapping("/getNearbyAttractions")
    public List<Attraction> getNearbyAttractions(@RequestParam String userName) {
        User user = gpsUtilService.getUser(userName);
        return gpsUtilService.getNearByAttractions(user);
    }
    @RequestMapping("/nearAttractions")
    public Boolean getNearAttraction(@RequestBody VisitedLocation visitedLocation, @RequestBody Attraction attraction) {
        Boolean nearAttraction = gpsUtilService.nearAttraction(visitedLocation, attraction);
        return nearAttraction;
    }

    @RequestMapping("/isWithinAttractionProximity")
    public Boolean isWithinAttractionProximity(@RequestBody Attraction attraction, Location location) {
        Boolean withinAttractionProximity = gpsUtilService.isWithinAttractionProximity(attraction,location);
        return withinAttractionProximity;
    }

    @RequestMapping("/getCurrentLocation")
    public VisitedLocation getCurrentLocation(@RequestParam String userName) {
        User user = gpsUtilService.getUser(userName);
        VisitedLocation visitedLocation = user.getLastVisitedLocation();
        return visitedLocation;
    }

    @GetMapping(value = "/getAllAttractions")
    public List<Attraction> getAllAttractions(){
        List<Attraction>getAttractionsList = gpsUtilService.getAllAttractions();
        return getAttractionsList;
    }

    @GetMapping(value = "/trackUserLocation")
    public VisitedLocation trackUserLocation(@RequestParam String userName) {
        User user = gpsUtilService.getUser(userName);
        VisitedLocation visitedLocation = gpsUtilService.trackUserLocation(user);
        return visitedLocation;
    }


}
