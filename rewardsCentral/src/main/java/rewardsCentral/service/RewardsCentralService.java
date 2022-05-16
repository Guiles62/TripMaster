package rewardsCentral.service;


import rewardsCentral.model.Attraction;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * <b>RewardsCentralService is the interface that will implemented by RewardsCentralServiceImpl</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getUserRewards</li>
 *         <li>getAttractionsRewardPoints</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
public interface RewardsCentralService {


    List<UserReward> getUserRewards(User user);
    int getAttractionRewardPoints(UUID userId, UUID attractionID);
    List<User> getAllUsersRewards(List<User> users) throws ExecutionException, InterruptedException;


}
