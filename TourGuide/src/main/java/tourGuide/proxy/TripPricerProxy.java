package tourGuide.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;

@FeignClient(name = "tripPricer", url = "localhost:8083")
public interface TripPricerProxy {

    @GetMapping(value = "/getTripDeals")
    List<Provider>getTripDeals(@RequestBody User user, @RequestParam String apiKey, @RequestParam int rewardsPoints);

    @GetMapping(value = "/getPrice")
    List<Provider>getPrice(@RequestBody User user,@RequestParam String url,@RequestParam int rewardsPoints);
}
