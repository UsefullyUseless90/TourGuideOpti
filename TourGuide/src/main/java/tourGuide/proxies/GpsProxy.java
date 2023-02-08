package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import java.util.List;
import java.util.UUID;

@Component
@FeignClient(name = "GpsApp", url = "http://localhost:8090")
public interface GpsProxy {

    /**
     * This is going to hit the endpoint "/getUserLocation" to get "application/json"
     * at the GPS API that's running at "http://localhost:8090"
     * @param userId
     * @return the location of a user using his id (json format)
     */
    @RequestMapping(value = "/getUserLocation", produces = "application/json")
    VisitedLocation getLocation(@RequestParam UUID userId);

    /**
     * This is going to hit the endpoint "/getAttractions" to get "application/json"
     * at the GPS API that's running at "http://localhost:8090"
     * @return a list of attractions (json format)
     */
    @RequestMapping(value = "/getAttractions", produces = "application/json")
    List<Attraction> getAttractions();
}
