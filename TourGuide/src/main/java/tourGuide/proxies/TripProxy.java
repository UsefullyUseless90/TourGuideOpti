package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

@Component
@FeignClient(name = "tripPricerApp", url = "http://localhost:8092")
public interface TripProxy {

    /**
     * This is going to hit the endpoint "/tripPricer/{apiKey}/{attractionId}/{adults}/{children}/{nightsStay}/{rewardsPoints}" to get "application/json"
     * at the GPS API that's running at "http://localhost:8092"
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return a list of prices
     */
    @GetMapping("/tripPricer/{apiKey}/{attractionId}/{adults}/{children}/{nightsStay}/{rewardsPoints}")
    List<Provider> getPrice(@PathVariable("apiKey") String apiKey, @PathVariable("attractionId") UUID attractionId, @PathVariable("adults") int adults,
                            @PathVariable("children") int children, @PathVariable("nightsStay") int nightsStay, @PathVariable("rewardsPoints") int rewardsPoints);

}
