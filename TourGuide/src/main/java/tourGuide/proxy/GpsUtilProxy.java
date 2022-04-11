package tourGuide.proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.user.User;

import java.util.List;
import java.util.UUID;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @RequestMapping(value = "/getLocation")
    Location getLocation(@RequestParam UUID userId);

    @RequestMapping("/getUserVisitedLocation")
    List<VisitedLocation> getUserVisitedLocation(@RequestParam UUID userId);

    @RequestMapping(value = "/getNearbyAttractions")
    List<Attraction> getNearbyAttractions(@RequestParam UUID userId);

    @RequestMapping(value = "/getCurrentLocation")
    VisitedLocation getCurrentLocation(@RequestParam UUID userId);

    @GetMapping(value = "/getAllAttractions")
    List<Attraction> getAllAttractions();

    @GetMapping(value = "/trackUserLocation")
    VisitedLocation trackUserLocation(@RequestParam UUID userId);

    @RequestMapping("/nearAttraction")
    Boolean nearAttraction (@RequestParam VisitedLocation visitedLocation, @RequestParam Attraction attraction);

}
