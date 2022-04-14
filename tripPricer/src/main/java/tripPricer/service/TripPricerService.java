package tripPricer.service;


import tripPricer.model.Provider;
import tripPricer.model.User;

import java.util.List;


public interface TripPricerService {

    List<Provider> getTripDeals(User user, String url);
    List<Provider> getPrice(User user, String url);
}
