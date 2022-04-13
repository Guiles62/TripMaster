package rewardsCentral.service;


import rewardsCentral.model.Attraction;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;

import java.util.List;
import java.util.UUID;

public interface RewardsCentralService {


    List<UserReward> getUserRewards(User user);
    int getRewardsPointsSum(User user);
    int getAttractionRewardPoints(UUID userId, UUID attractionID);


}
