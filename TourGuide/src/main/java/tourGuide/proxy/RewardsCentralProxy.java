package tourGuide.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.model.Attraction;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "rewardsCentral", url = "rewardscentral:8082")
public interface RewardsCentralProxy {

    @PostMapping(value = "/getRewards")
    List<UserReward> getRewards(@RequestBody User user);

    @PostMapping("/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam UUID userId,@RequestParam UUID attractionId);

    @RequestMapping("/getAttractions")
    List<Attraction> getAttractions();

}
