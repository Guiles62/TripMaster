package tripPricer.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tripPricer.Provider;
import tripPricer.service.TripPricerService;

import java.util.List;

public class TripPricerController {

    TripPricerService tripPricerService;

    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tripPricerService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

}
