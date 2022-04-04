package rewardsCentral.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String getRewardsPoint(@RequestBody User user ) {
        return JsonStream.serialize(rewardsCentralService.getUserRewards(user));
    }

    @RequestMapping("/getUserRewardsPointsSum")
    public String getRewardsPointSum(@RequestBody User user, List<UserReward> userRewardList) {
        return JsonStream.serialize(rewardsCentralService.getRewardsPointsSum(user, userRewardList));
    }

    @RequestMapping("/getAttractionRewardPoints")
    public String getAttractionRewardPoints(@RequestBody User user, UUID attractionId) {
        return JsonStream.serialize(rewardsCentralService.getAttractionRewardPoints(user.getUserId(), attractionId));
    }


}
