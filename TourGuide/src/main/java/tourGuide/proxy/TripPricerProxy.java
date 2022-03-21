package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;

@FeignClient(name = "tripPricer", url = "localhost:8083")
public interface TripPricerProxy {

    @GetMapping(value = "/getTripDeals")
    List<Provider>getTripDeals(User user, String apiKey, int rewardsPoints);

    @GetMapping(value = "/getPrice")
    List<Provider>getPrice(User user, String url, int rewardsPoints);
}
