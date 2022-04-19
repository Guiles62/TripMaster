package tripPricer.controller;


import org.springframework.web.bind.annotation.*;
import tripPricer.model.Provider;
import tripPricer.model.User;
import tripPricer.service.TripPricerService;

import java.util.List;

@RestController
public class TripPricerController {

    private TripPricerService tripPricerService;


    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    @PostMapping(value = "/getPrice")
    public List<Provider> getPrice(@RequestBody User user, @RequestParam String url) {
        List<Provider> providers = tripPricerService.getTripDeals(user, url);
        return providers;
    }

}
