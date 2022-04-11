package rewardsCentral.service;


import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;

import java.util.List;
import java.util.UUID;

public interface RewardsCentralService {

    User getUser(UUID userid);
    List<UserReward> getUserRewards(User user);
    int getRewardsPointsSum(User user, List<UserReward> userRewards);
    int getAttractionRewardPoints(UUID attractionId, UUID userId);

}
