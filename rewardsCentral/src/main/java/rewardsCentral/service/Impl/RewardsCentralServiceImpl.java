package rewardsCentral.service.Impl;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import rewardsCentral.model.*;
import rewardsCentral.service.RewardsCentralService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RewardsCentralServiceImpl implements RewardsCentralService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int defaultProximityBuffer = 3000;
    private int proximityBuffer = defaultProximityBuffer;
    RewardCentral rewardCentral = new RewardCentral();

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public RewardsCentralServiceImpl() {
    }


    @Override
    public List<UserReward> getUserRewards(User user) {
        calculateRewards(user);
        return user.getUserRewards();
    }

    @Override
    public int getAttractionRewardPoints(UUID userID,UUID attractionId) {
        int rewards = rewardCentral.getAttractionRewardPoints(userID,attractionId);
        return rewards;
    }

    @Override
    public int getRewardsPointsSum(User user) {
        List<UserReward> userRewards = user.getUserRewards();
        int sum = 0;
        for (UserReward userReward : userRewards){
            sum += userReward.getRewardPoints();
        }
        return sum;
    }

    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = user.getAttractions();
        List<UserReward> userRewards = new ArrayList<>();
        for(VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        userRewards.add(new UserReward(visitedLocation, attraction, getAttractionRewardPoints(attraction.attractionId, user.getUserId())));
                        user.setUserRewards(userRewards);
                    }
                }
            }
        }
    }

    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }
}
