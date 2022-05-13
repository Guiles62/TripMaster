package tourGuide.proxy;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;


import java.util.List;
import java.util.UUID;

@FeignClient( name = "gpsUtil", url = "localhost:8081")
public interface GpsUtilProxy {

    @PostMapping(value = "/getLocation")
    Location getLocation(@RequestParam UUID userId);

    @PostMapping("/getUserVisitedLocation")
    List<VisitedLocation> getUserVisitedLocation(@RequestParam UUID userId);

    @PostMapping(value = "/getNearbyAttractions")
    List<Attraction> getNearbyAttractions(@RequestParam UUID userId);

    @PostMapping(value = "/getCurrentLocation")
    VisitedLocation getCurrentLocation(@RequestParam UUID userId);

    @GetMapping(value = "/getAllAttractions")
    List<Attraction> getAllAttractions();

    @PostMapping(value = "/trackUserLocation")
    VisitedLocation trackUserLocation(@RequestParam UUID userId);

    @PostMapping (value = "/getDistance")
    double getDistance(@RequestParam  double latitude1, @RequestParam double longitude1, @RequestParam double latitude2, @RequestParam double longitude2);


}
