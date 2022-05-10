package tourGuide.controller;


import java.util.List;
import java.util.concurrent.ExecutionException;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import tourGuide.model.*;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

/**
 * <b>TourGuideController call TourGuideService to display data</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>index</li>
 *         <li>getLocation</li>
 *         <li>getNearbyAttractions</li>
 *         <li>getRewards</li>
 *         <li>getAllCurrentLocations</li>
 *         <li>getTripDeals</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */

@RestController
public class TourGuideController {

	TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    /**
     * method which display index message
     * @return Greetings from TourGuide!
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * call service to find the user location
     * @param userName is the username of user
     * @return a JSON object that contains longitude and latitude
     */
    @GetMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
    	Location location = tourGuideService.getLocation(user);
		return location;
    }

    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral

    /**
     * call service to display the closest five tourist attractions to the user
     * @param userName is the username of user
     * @return a JSON object that display the location, name, city, state and attractionId of the closest 5 attractions
     */
    @GetMapping("/getNearByAttractions")
    public List<NearByAttractions> getNearByAttractions(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
    	List<NearByAttractions> nearAttractions = tourGuideService.getNearByAttractions(user);
    	return nearAttractions;
    }

    /**
     * call service to display user rewards
     * @param userName is the username of user
     * @return a JSON object that display user's visitedLocation, attractions and show rewardsPoints
     */
    @GetMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) throws ExecutionException, InterruptedException {
        User user = tourGuideService.getUser(userName);
        List<UserReward> getUserRewardsList = tourGuideService.getRewards(user);
    	return getUserRewardsList;
    }


    /**
     * call service to display location of all users
     * @return a JSON object that display all users by userId and their locations
     */
    @GetMapping("/getAllCurrentLocations")
    public List<VisitedLocation> getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
        List<VisitedLocation>usersCurrentVisitedLocationList = tourGuideService.getAllCurrentLocations();
    	return usersCurrentVisitedLocationList;
    }

    /**
     * call service to display user's tripDeals
     * @param userName is the username of user
     * @return a JSON object that display user's tripDeals with name of attraction, price for the trip and tripId
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
    	List<Provider> providers = tourGuideService.getTripDeals(user);
    	return providers;
    }

}