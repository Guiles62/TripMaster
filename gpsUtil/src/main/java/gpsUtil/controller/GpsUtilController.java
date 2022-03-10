package gpsUtil.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import gpsUtil.model.User;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


public class GpsUtilController {

    GpsUtilService gpsUtilService;

    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName, User user) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user.getUserName(userName)); // où aller chercher les infos du user ??
        return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(getUser(userName));
        return JsonStream.serialize(gpsUtilService.getNearByAttractions(visitedLocation));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }

        return JsonStream.serialize("");
    }



}
