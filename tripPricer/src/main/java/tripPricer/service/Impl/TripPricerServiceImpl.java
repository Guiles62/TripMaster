package tripPricer.service.Impl;


import org.springframework.stereotype.Service;
import tripPricer.TripPricer;
import tripPricer.model.Provider;
import tripPricer.model.User;
import tripPricer.model.UserReward;
import tripPricer.service.TripPricerService;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>TripPricerServiceImpl is the class which implement TripPricerService and call TripPricer jar</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getTripDeals</li>
 *         <li>getPrice</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@Service
public class TripPricerServiceImpl implements TripPricerService {

    private TripPricer tripPricer = new TripPricer();

    public TripPricerServiceImpl() {
    }


    /**
     * call the method getPrice to calculate a list of provider
     * @param user the user we use
     * @param apiKey the apiKey we use
     * @return a list of provider
     */
    @Override
    public List<Provider> getTripDeals(User user, String apiKey) {
        List<Provider> providers = getPrice(user, apiKey);
        user.setTripDeals(providers);
        return providers;
    }

    /**
     * calculate the user's provider
     * @param user the user we use
     * @param url the url we use
     * @return a list of provider calculate with the user's rewards
     */
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
