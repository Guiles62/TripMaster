package gpsUtil.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class GpsUtilController {

    private GpsUtilService gpsUtilService;

    public GpsUtilController() {
    }

    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestBody User user) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestBody User user) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return JsonStream.serialize(gpsUtilService.getNearByAttractions(visitedLocation));
    }
    @RequestMapping("/nearAttractions")
    public String getNearAttraction(@RequestBody VisitedLocation visitedLocation, @RequestBody Attraction attraction) {
        Boolean nearAttraction = gpsUtilService.nearAttraction(visitedLocation, attraction);
        return JsonStream.serialize(nearAttraction);
    }

    @RequestMapping("/isWithinAttractionProximity")
    public String isWithinAttractionProximity(@RequestBody Attraction attraction, Location location) {
        Boolean withinAttractionProximity = gpsUtilService.isWithinAttractionProximity(attraction,location);
        return JsonStream.serialize(withinAttractionProximity);
    }

    @RequestMapping("/getCurrentLocation")
    public String getCurrentLocation(@RequestBody  User user) {
        VisitedLocation visitedLocation = user.getLastVisitedLocation();
        return JsonStream.serialize(visitedLocation);
    }

    @GetMapping(value = "/getAllAttractions")
    public String getAllAttractions(){
        List<Attraction>getAttractionsList = gpsUtilService.getAllAttractions();
        return JsonStream.serialize(getAttractionsList);
    }

    @GetMapping(value = "/trackUserLocation")
    public String trackUserLocation(@RequestBody User user) {
        VisitedLocation visitedLocation = gpsUtilService.trackUserLocation(user);
        return JsonStream.serialize(visitedLocation);
    }


}
