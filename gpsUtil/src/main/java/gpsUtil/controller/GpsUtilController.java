package gpsUtil.controller;


import gpsUtil.model.*;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

/**
 * <b>GpsUtilController call GpsUtilService to get Data</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getLocation</li>
 *         <li>getUserVisitedLocation</li>
 *         <li>getNearbyAttractions</li>
 *         <li>getCurrentLocation</li>
 *         <li>getAllAttractions</li>
 *         <li>trackUserLocation</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@RestController
public class GpsUtilController {

    private GpsUtilService gpsUtilService;


    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    /**
     * call gpsUtilService to get user's location
     * @param userId the user's id
     * @return the user's location
     */
    @PostMapping ("/getLocation")
    public Location getLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user);
        return visitedLocation.location;
    }

    /**
     * get a list of user's visitedLocation
     * @param userId the user's id
     * @return a list of user's visitedLocation
     */
    @PostMapping("/getUserVisitedLocation")
    public List<VisitedLocation> getUserVisitedLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        List<VisitedLocation> userVisitedLocations = user.getVisitedLocations();
        return userVisitedLocations;
    }

    /**
     * call the gpsUtilService to get the user and find the closest 5 attractions near him
     * @param userId the user's id
     * @return a list of 5 attractions
     */
    @PostMapping("/getNearbyAttractions")
    public List<Attraction> getNearbyAttractions(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        List<Attraction> attractions = gpsUtilService.getNearByAttractions(user);
        return attractions;
    }

    /**
     * call the gpsUtilService to get the user and get his last visitedLocation
     * @param userId the user's id
     * @return the user's last visitedLocation
     */
    @PostMapping("/getCurrentLocation")
    public VisitedLocation getCurrentLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = user.getLastVisitedLocation();
        return visitedLocation;
    }

    /**
     * call the gpsUtilService to get all attractions
     * @return a list of all attractions
     */
    @GetMapping(value = "/getAllAttractions")
    public List<Attraction> getAllAttractions(){
        List<Attraction>getAttractionsList = gpsUtilService.getAllAttractions();
        return getAttractionsList;
    }

    /**
     * call the gpsUtilService to get the user and track his visitedLocation
     * @param userId the user's id
     * @return the user's visitedLocation
     */
    @PostMapping(value = "/trackUserLocation")
    public VisitedLocation trackUserLocation(@RequestParam UUID userId) {
        User user = gpsUtilService.getUser(userId);
        VisitedLocation visitedLocation = gpsUtilService.trackUserLocation(user);
        return visitedLocation;
    }

    @PostMapping(value="/trackAllUsersLocation")
    public List<VisitedLocation> trackAllUsersLocation (@RequestBody List<User> users) {
        List<VisitedLocation> allUsersTrackLocation = gpsUtilService.trackAllUserLocation(users);
        return allUsersTrackLocation;
    }

    @PostMapping(value = "/getDistance")
    public double getDistance (@RequestParam  double latitude1, @RequestParam double longitude1, @RequestParam double latitude2, @RequestParam double longitude2) {
        Location loc1 = new Location(latitude1,longitude1);
        Location loc2 = new Location(latitude2,longitude2);
        return gpsUtilService.getDistance(loc1,loc2);
    }


}
