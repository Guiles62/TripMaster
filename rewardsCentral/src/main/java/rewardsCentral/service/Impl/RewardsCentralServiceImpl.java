package rewardsCentral.service.Impl;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import rewardsCentral.model.*;
import rewardsCentral.service.RewardsCentralService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * <b>RewardsCentralServiceImpl is the class which implement RewardsCentralService and call RewardCentral jar</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getUserRewards</li>
 *         <li>getAttractionRewardPoints</li>
 *         <li>calculateRewards</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
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


    /**
     * call the calculateRewards method to get the user's rewards
     * @param user the user we use
     * @return a list of userReward
     */
    @Override
    public List<UserReward> getUserRewards(User user) {
        calculateRewards(user);
        return user.getUserRewards();
    }

    @Override
    public List<User> getAllUsersRewards(List<User> users) {
        ExecutorService executorService = Executors.newFixedThreadPool(100000);
        List<User> userWithRewards = new CopyOnWriteArrayList<>();
        for(User user : users) {
            userWithRewards.add(user);
            CompletableFuture<List<UserReward>> result = CompletableFuture.supplyAsync( () -> getUserRewards(user), executorService);
            CompletableFuture<Void> result2 = result.thenAccept( s -> user.setUserRewards(s));
            result2.join();
        }
        executorService.shutdown();
        return userWithRewards;
    }

    /**
     * call RewardCentral jar to get attraction points for a user
     * @param userID the user id
     * @param attractionId the attraction id
     * @return a Integer rewards
     */
    @Override
    public int getAttractionRewardPoints(UUID userID,UUID attractionId) {
        int rewards = rewardCentral.getAttractionRewardPoints(userID,attractionId);
        return rewards;
    }


    /**
     * calculate the user's rewards
     * @param user the user we use
     */
    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = user.getAttractions();
        List<UserReward> userRewards = new ArrayList<>();
        for(VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    userRewards.add(new UserReward(visitedLocation, attraction, getAttractionRewardPoints(attraction.attractionId, user.getUserId())));
                    user.setUserRewards(userRewards);
                }
            }
        }
    }

    /**
     * indicate if a distance between a user's visitedLocation and an attraction is greater than a proximityBuffer
     * @param visitedLocation the user's visitedLocation
     * @param attraction the attraction
     * @return a boolean that indicates if the distance is greater than proximityBuffer or not
     */
    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    /**
     * calculate the distance between 2 points
     * @param loc1 is the first location
     * @param loc2 is the second location
     * @return a distance in nautical miles
     */
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
