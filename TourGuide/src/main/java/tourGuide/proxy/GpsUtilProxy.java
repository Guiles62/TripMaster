package tourGuide.proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tourGuide.user.User;

import java.util.List;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @GetMapping(value = "/getLocation")
    VisitedLocation getLocation(User user);

    @GetMapping(value = "/getNearbyAttractions")
    List<Attraction> getNearbyAttractions(User user);

    @GetMapping(value = "/getCurrentLocation")
    VisitedLocation getCurrentLocation(User user);

    @GetMapping(value = "/getAllAttractions")
    List<Attraction> getAllAttractions();

}
