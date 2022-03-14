package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tripPricer", url = "localhost:8083")
public interface TripPricerProxy {

    @GetMapping(value = "/getTripDeals")
    String getTripDeals(@RequestParam String userName);
}
