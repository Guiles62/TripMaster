package tripPricer.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import tripPricer.model.User;
import tripPricer.service.TripPricerService;

import java.util.List;

@RestController
public class TripPricerController {

    private TripPricerService tripPricerService;


    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestBody User user, @RequestParam String apiKey, @RequestParam int rewardsPoints) {
        List<Provider> providers = tripPricerService.getTripDeals(user, apiKey, rewardsPoints);
        return JsonStream.serialize(providers);
    }
    @GetMapping(value = "/getPrice")
    public String getPrice(@RequestBody User user, @RequestParam String url, @RequestParam int rewardsPoints) {
        List<Provider> providers = tripPricerService.getPrice(user, url, rewardsPoints);
        return JsonStream.serialize(providers);
    }

}
