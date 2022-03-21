package gpsUtil.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.Attraction;
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
    @RequestMapping("/getNearAttractions")
    public String getNearAttractions(@RequestBody User user) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return JsonStream.serialize(gpsUtilService.getNearAttractions(visitedLocation));
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


}
