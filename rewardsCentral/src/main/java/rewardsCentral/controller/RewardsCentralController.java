package rewardsCentral.controller;


import org.springframework.web.bind.annotation.*;
import rewardsCentral.model.Attraction;
import rewardsCentral.model.User;
import rewardsCentral.model.UserReward;
import rewardsCentral.service.RewardsCentralService;

import java.util.List;
import java.util.UUID;

/**
 * <b>RewardsCentralController RewardsCentralService to get data</b>
 * <p>
 *     contains methods
 *     <ul>
 *         <li>getRewards</li>
 *     </ul>
 * </p>
 * @author Guillaume C
 */
@RestController
public class RewardsCentralController {

    private RewardsCentralService rewardsCentralService;


    public RewardsCentralController(RewardsCentralService rewardsCentralService) {
        this.rewardsCentralService = rewardsCentralService;
    }

    /**
     * call the rewardsCentralService to get user's rewards
     * @param user the user we use
     * @return a list of userRewards
     */
    @PostMapping(value = "/getRewards")
    public List<UserReward> getRewards(@RequestBody User user) {
        return rewardsCentralService.getUserRewards(user);
    }

    /*@RequestMapping("/getUserRewardsPointsSum")
    public int getRewardsPointSum(@RequestBody User user) {
        return rewardsCentralService.getRewardsPointsSum(user);
    }*/

    @RequestMapping("/getAttractionRewardPoints")
    public int getAttractionRewardPoints(@RequestParam UUID userId, @RequestParam UUID attractionId) {
        return rewardsCentralService.getAttractionRewardPoints(userId, attractionId);
    }

}
