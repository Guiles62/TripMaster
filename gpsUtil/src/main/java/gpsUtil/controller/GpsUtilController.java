package gpsUtil.controller;


import gpsUtil.model.Attraction;
import gpsUtil.model.VisitedLocation;
import gpsUtil.model.Location;
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

    @PostMapping ("/getLocation")
    public Location getLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return visitedLocation.location;
    }

    @PostMapping("/getUserVisitedLocation")
    public List<VisitedLocation> getUserVisitedLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        List<VisitedLocation> userVisitedLocations = user.getVisitedLocations();
        return userVisitedLocations;
    }

    @PostMapping("/getNearbyAttractions")
    public List<Attraction> getNearbyAttractions(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        return gpsUtilService.getNearByAttractions(user);
    }

    @PostMapping("/nearAttractions")
    public Boolean getNearAttraction(@RequestBody VisitedLocation visitedLocation, @RequestBody Attraction attraction) {
        Boolean nearAttraction = gpsUtilService.nearAttraction(visitedLocation, attraction);
        return nearAttraction;
    }

    @PostMapping("/getCurrentLocation")
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

    @PostMapping(value = "/trackUserLocation")
    public VisitedLocation trackUserLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.trackUserLocation(user);
        return visitedLocation;
    }


}
