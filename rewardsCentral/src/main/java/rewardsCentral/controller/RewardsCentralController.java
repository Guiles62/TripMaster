package rewardsCentral.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rewardsCentral.service.RewardsCentralService;

public class RewardsCentralController {

    RewardsCentralService rewardsCentralService;

    public RewardsCentralController(RewardsCentralService rewardsCentralService) {
        this.rewardsCentralService = rewardsCentralService;
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(rewardsCentralService.getUserRewards(getUser(userName)));
    }

}
