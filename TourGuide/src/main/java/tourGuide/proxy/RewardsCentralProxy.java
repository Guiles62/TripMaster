package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.user.User;

@FeignClient(name = "rewardsCentral", url = "localhost:8082")
public interface RewardsCentralProxy {

    @GetMapping(value = "/getRewards")
    String getRewards(@RequestParam String userName, User user);
}
