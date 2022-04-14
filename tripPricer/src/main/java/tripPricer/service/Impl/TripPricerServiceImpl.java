package tripPricer.service.Impl;


import org.springframework.stereotype.Service;
import tripPricer.TripPricer;
import tripPricer.model.Provider;
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


    @Override
    public List<Provider> getTripDeals(User user, String apiKey) {
        List<Provider> providers = getPrice(user, apiKey);
        user.setTripDeals(providers);
        return providers;
    }

    @Override
    public List<Provider> getPrice(User user, String url) {
        List<Provider> providerList = new ArrayList<>();
        List<UserReward> userRewards = user.getUserRewards();
        int rewardsPoints = 0;
        for (UserReward userReward : userRewards) {
            rewardsPoints += userReward.getRewardPoints();
        }
        int rewardsPointsForPrice = rewardsPoints/10;
        List<tripPricer.Provider> providers = tripPricer.getPrice(url,user.getUserId(),user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),rewardsPointsForPrice);
        for (tripPricer.Provider provider : providers) {
            Double price = (provider.price*100)/100;
            providerList.add(new Provider(provider.tripId, provider.name, price));
        }
        return providerList;
    }
}
