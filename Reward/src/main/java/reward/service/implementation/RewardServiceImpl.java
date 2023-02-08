package reward.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reward.service.IRewardService;
import rewardCentral.RewardCentral;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class RewardServiceImpl implements IRewardService {

    /**
     * Util from the librairie that has been configured
     */
    private final RewardCentral rewardCentral = new RewardCentral();

    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(RewardServiceImpl.class);

    /**
     * Get the reward points
     * @param attractionId
     * @param userId
     * @return reward points of attractions
     */
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getAttractionsRewardPoints(UUID attractionId, UUID userId) {
        logger.info("This is the total of reward points:" + rewardCentral.getAttractionRewardPoints(attractionId, userId));
        return CompletableFuture.completedFuture(rewardCentral.getAttractionRewardPoints(attractionId, userId));
    }
}
