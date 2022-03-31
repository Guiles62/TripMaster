package tourGuide.proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.user.User;

import java.util.List;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @RequestMapping(value = "/getLocation")
    VisitedLocation getLocation(@RequestBody User user);

    @RequestMapping(value = "/getNearbyAttractions")
    List<Attraction> getNearbyAttractions(@RequestBody User user);

    @RequestMapping(value = "/getCurrentLocation")
    VisitedLocation getCurrentLocation(@RequestBody User user);

    @GetMapping(value = "/getAllAttractions")
    List<Attraction> getAllAttractions();

    @RequestMapping("/getNearAttractions")
    List<Attraction> getNearAttractions(@RequestBody User user);

    @RequestMapping("/trackUserLocation")
    VisitedLocation trackUserLocation (@RequestBody User user);

    @RequestMapping("/nearAttraction")
    Boolean nearAttraction (@RequestParam VisitedLocation visitedLocation, @RequestParam Attraction attraction);

    @RequestMapping("/isWithinAttractionProximity")
    Boolean isWithinAttractionProximity (@RequestParam Attraction attraction, Location location);
}
