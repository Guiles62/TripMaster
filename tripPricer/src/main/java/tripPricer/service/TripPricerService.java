package tripPricer.service;

import tripPricer.Provider;
import tripPricer.model.User;

import java.util.List;


public interface TripPricerService {

    List<Provider> getTripDeals(User user);
}
