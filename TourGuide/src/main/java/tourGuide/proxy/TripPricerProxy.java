package tourGuide.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Provider;
import tourGuide.user.User;


import java.util.List;

@FeignClient(name = "tripPricer", url = "trippricer:8083")
public interface TripPricerProxy {


    @PostMapping(value = "/getPrice")
    List<Provider> getPrice(@RequestBody User user, @RequestParam String url);
}
