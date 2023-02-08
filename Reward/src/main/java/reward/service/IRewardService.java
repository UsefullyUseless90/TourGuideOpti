package reward.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;



public interface IRewardService {

    CompletableFuture<Integer> getAttractionsRewardPoints(UUID attractionId, UUID userId);

}
