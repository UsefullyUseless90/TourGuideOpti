package trippricer.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;
import trippricer.service.ITripPricerService;

import java.util.List;
import java.util.UUID;

@Service
public class TripPricerServiceImpl implements ITripPricerService {

    @Autowired
    private TripPricer tripPricer;

    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(TripPricerServiceImpl.class);

    /**
     * Method to get prices using multiple params
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return a list of prices
     */
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        logger.info("This the list of attractions's prices:" + tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints));
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }
}
