package rewardsCentral.service;


import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;

import java.util.List;

public interface RewardsCentralService {

    void calculateRewards(User user);
    List<UserReward> getUserRewards(User user);

}
