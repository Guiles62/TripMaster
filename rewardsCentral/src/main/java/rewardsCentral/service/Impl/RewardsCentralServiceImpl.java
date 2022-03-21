package rewardsCentral.service.Impl;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.service.RewardsCentralService;

import java.util.List;
import java.util.UUID;

@Service
public class RewardsCentralServiceImpl implements RewardsCentralService {

    RewardCentral rewardCentral;

    public RewardsCentralServiceImpl() {
    }

    public RewardsCentralServiceImpl(RewardCentral rewardCentral) {
        this.rewardCentral = rewardCentral;
    }

    @Override
    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public int getAttractionRewardPoints(UUID userId,UUID attractionId) {
        return rewardCentral.getAttractionRewardPoints(userId, attractionId);
    }

    @Override
    public int getRewardsPointsSum(User user, List<UserReward> userRewards) {
        userRewards = user.getUserRewards();
        int sum = 0;
        for (UserReward userReward : userRewards){
            sum += userReward.getRewardPoints();
        }
        return sum;
    }
}
