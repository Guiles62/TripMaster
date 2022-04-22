package tripPricer.controller;


import org.springframework.web.bind.annotation.*;
import tripPricer.model.Provider;
import tripPricer.model.User;
import tripPricer.service.TripPricerService;

import java.util.List;

/**
 * <b>TripPricerController call TripPricerService to get data</b>
 * <p>
 *     contains method
 *     <ul>
 *         <li>getPrice</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@RestController
public class TripPricerController {

    private TripPricerService tripPricerService;


    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    /**
     * call the tripPricerService to get the user's tripDeals
     * @param user the user we use
     * @param url the url we use
     * @return a list of provider
     */
    @PostMapping(value = "/getPrice")
    public List<Provider> getPrice(@RequestBody User user, @RequestParam String url) {
        List<Provider> providers = tripPricerService.getTripDeals(user, url);
        return providers;
    }

}
