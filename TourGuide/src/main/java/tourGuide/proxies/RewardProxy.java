package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@FeignClient(name = "RewardApp", url = "http://localhost:8088", decode404 = true)
public interface RewardProxy {

    /**
     * This is going to hit the endpoint "/reward/RewardPoints/{attractionId}/{userId}" to get "application/json"
     * at the GPS API that's running at "http://localhost:8088"
     * @param attractionId
     * @param userId
     * @return result of reward.getAttractionsRewardPoints method (json format)
     */
    @GetMapping("/reward/RewardPoints/{attractionId}/{userId}")
    CompletableFuture<Integer> getAttractionRewardPoints(
            @PathVariable("attractionId") UUID attractionId,
            @PathVariable("userId") UUID userId
    );
}
