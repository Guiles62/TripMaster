package rewardsCentral.service.Impl;

import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.service.RewardsCentralService;

import java.util.List;

public class RewardsCentralServiceImpl implements RewardsCentralService {

    @Override
    public void calculateRewards(User user) {

    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }
}
