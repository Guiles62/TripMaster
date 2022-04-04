package tourGuide.controller;


import java.util.List;


import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;



import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName, User user) {
        user = tourGuideService.getUser(userName);
    	VisitedLocation visitedLocation = tourGuideService.getLocation(user);
		return JsonStream.serialize(visitedLocation.location);
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
    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName, User user) {
        user = tourGuideService.getUser(userName);
    	List<Attraction> nearAttractions = tourGuideService.getNearbyAttractions(user);
    	return JsonStream.serialize(nearAttractions);
    }

    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName, User user) {
        user = tourGuideService.getUser(userName);
        List<UserReward> getUserRewardsList = tourGuideService.getRewards(user);
    	return JsonStream.serialize(getUserRewardsList);
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
        List<VisitedLocation>usersCurrentVisitedLocationList = tourGuideService.getAllCurrentLocations();
    	return JsonStream.serialize(usersCurrentVisitedLocationList);
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
    	List<Provider> providers = tourGuideService.getTripDeals(user);
    	return JsonStream.serialize(providers);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
        return user;
    }


}