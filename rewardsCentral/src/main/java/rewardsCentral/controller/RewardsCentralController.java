package rewardsCentral.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.service.RewardsCentralService;

import java.util.List;
import java.util.UUID;

@RestController
public class RewardsCentralController {

    private RewardsCentralService rewardsCentralService;


    public RewardsCentralController(RewardsCentralService rewardsCentralService) {
        this.rewardsCentralService = rewardsCentralService;
    }

    @RequestMapping("/getRewards")
    public List<UserReward> getRewardsPoint(@RequestParam UUID userId, @RequestParam UUID attractionId) {
        User user = rewardsCentralService.getUser(userId);
        List<UserReward> userRewardList = rewardsCentralService.getUserRewards(user);
        return userRewardList;
    }

    @RequestMapping("/getUserRewardsPointsSum")
    public int getRewardsPointSum(@RequestBody User user, List<UserReward> userRewardList) {
        return rewardsCentralService.getRewardsPointsSum(user, userRewardList);
    }

    @RequestMapping("/getAttractionRewardPoints")
    public int getAttractionRewardPoints(@RequestBody User user, UUID attractionId) {
        return rewardsCentralService.getAttractionRewardPoints(user.getUserId(), attractionId);
    }


}
