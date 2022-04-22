package tripPricer.service;


import tripPricer.model.Provider;
import tripPricer.model.User;

import java.util.List;

/**
 * <b>TripPricerService is the interface that will implemented by TripPricerServiceImpl</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getTripDeals</li>
 *         <li>getPrice</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
public interface TripPricerService {

    List<Provider> getTripDeals(User user, String url);
    List<Provider> getPrice(User user, String url);
}
