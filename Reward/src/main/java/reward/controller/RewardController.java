package reward.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reward.service.IRewardService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class RewardController {

    @Autowired
    IRewardService rewardService;
    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(RewardController.class);
    /**
     * Welcome screen of the Reward Api
     * @return a greeting message
     */
    @RequestMapping("/reward")
    public String index() {
        return "This is the tour guide reward, how can we help you?";
    }

    /**
     * Endpoint to get info for the rewardPoints of an attraction
     * @param attractionId
     * @param userId
     * @return result of reward.getAttractionsRewardPoints method
     */
    @RequestMapping("/RewardPoints/{attractionId}/{userId}")
    public CompletableFuture<Integer> getRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        logger.info("Research results are: " + rewardService.getAttractionsRewardPoints(attractionId, userId));
        return rewardService.getAttractionsRewardPoints(attractionId, userId);
    }
}
