package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "rewardsCentral", url = "localhost:8082")
public interface RewardsCentralProxy {

    @RequestMapping(value = "/getRewards")
    List<UserReward> getRewards(User user);

    @RequestMapping(value = "/getUserRewardsPointsSum")
    int getUserRewardsPointsSum(User user, List<UserReward> userRewardList);

    @RequestMapping("/getAttractionRewardPoints")
    int getAttractionRewardPoints(UUID userId,UUID attractionId);
}
