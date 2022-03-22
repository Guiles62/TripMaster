package tourGuide.proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tourGuide.user.User;

import java.util.List;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @RequestMapping(value = "/getLocation")
    VisitedLocation getLocation(User user);

    @RequestMapping(value = "/getNearbyAttractions")
    List<Attraction> getNearbyAttractions(User user);

    @RequestMapping(value = "/getCurrentLocation")
    VisitedLocation getCurrentLocation(User user);

    @GetMapping(value = "/getAllAttractions")
    List<Attraction> getAllAttractions();

    @RequestMapping("/getNearAttractions")
    List<Attraction> getNearAttractions(User user);

    @RequestMapping("/trackUserLocation")
    VisitedLocation trackUserLocation (User user);

    @RequestMapping("/nearAttraction")
    Boolean nearAttraction (VisitedLocation visitedLocation, Attraction attraction);

    @RequestMapping("/isWithinAttractionProximity")
    Boolean isWithinAttractionProximity (Attraction attraction, Location location);
}
