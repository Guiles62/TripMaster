package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.user.User;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @GetMapping(value = "/getLocation")
    String getLocation(User user);

    @GetMapping(value = "/getNearbyAttractions")
    String getNearbyAttractions(User user);

    @GetMapping(value = "/getAllCurrentLocations")
    String getAllCurrentLocations();

}
