package tripPricer.service.Impl;


import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;
import tripPricer.model.User;
import tripPricer.service.TripPricerService;

import java.util.List;
@Service
public class TripPricerServiceImpl implements TripPricerService {

    private TripPricer tripPricer;

    public TripPricerServiceImpl() {
    }

    public TripPricerServiceImpl(TripPricer tripPricer) {
        this.tripPricer = tripPricer;
    }

    @Override
    public List<Provider> getTripDeals(User user, String apiKey, int rewardsPoints) {
        List<Provider> providers = getPrice(user, apiKey, rewardsPoints);
        user.setTripDeals(providers);
        return providers;
    }

    @Override
    public List<Provider> getPrice(User user, String url, int rewardsPoints) {
        List<Provider> providers = tripPricer.getPrice(url,user.getUserId(),user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),rewardsPoints);
        return providers;
    }
}
