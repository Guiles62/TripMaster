package tripPricer.service.Impl;


import org.springframework.stereotype.Service;
import tripPricer.model.Provider;
import tripPricer.model.TripPricer;
import tripPricer.model.User;
import tripPricer.model.UserReward;
import tripPricer.service.TripPricerService;

import java.util.ArrayList;
import java.util.List;
@Service
public class TripPricerServiceImpl implements TripPricerService {

    private TripPricer tripPricer = new TripPricer();

    public TripPricerServiceImpl() {
    }


    /*@Override
    public List<Provider> getTripDeals(User user, String apiKey, int rewardsPoints) {
        List<Provider> providers = getPrice(user, apiKey, rewardsPoints);
        user.setTripDeals(providers);
        return providers;
    }*/

    @Override
    public List<Provider> getPrice(User user, String url) {
        List<Provider> providerList = new ArrayList<>();
        List<UserReward> userRewards = user.getUserRewards();
        int rewardsPoints = 0;
        for (UserReward userReward : userRewards) {
            rewardsPoints += userReward.getRewardPoints();
        }
        List<tripPricer.Provider> providers = tripPricer.getPrice(url,user.getUserId(),user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),rewardsPoints);
        for (tripPricer.Provider provider : providers) {
            providerList.add(new Provider(provider.tripId, provider.name, provider.price));
        }
        return providerList;
    }
}
