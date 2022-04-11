package rewardsCentral.service.Impl;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import rewardsCentral.model.Attraction;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.model.VisitedLocation;
import rewardsCentral.service.RewardsCentralService;

import java.util.List;
import java.util.UUID;

@Service
public class RewardsCentralServiceImpl implements RewardsCentralService {


    RewardCentral rewardCentral = new RewardCentral();

    public RewardsCentralServiceImpl() {
    }


    @Override
    public User getUser(UUID userid) {
        User user = new User(userid);
        return user;
    }

    @Override
    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    @Override
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
    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = getAttractions();
        for(VisitedLocation visitedLocation : userLocations) {
            for(Attraction attraction : attractions) {
                if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if(nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction,user)));
                    }
                }
            }
        }
    }
}
