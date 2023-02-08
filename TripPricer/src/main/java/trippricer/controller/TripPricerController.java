package trippricer.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import trippricer.service.ITripPricerService;
import trippricer.service.implementation.TripPricerServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {

    @Autowired
    private ITripPricerService tripPricerService;

    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(TripPricerController.class);

    /**
     * Welcome screen of the TripPricer Api
     * @return a greeting message
     */
    @RequestMapping("/trip")
    public String index() {
        return "This is the tour guide trip pricer, how can we help you?";
    }

    /**
     * Endpoint to get info for prices
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return a list of prices
     */
    @GetMapping("/{apiKey}/{attractionId}/{adults}/{children}/{nightsStay}/{rewardsPoints}")
    public ResponseEntity<List<Provider>> getPrice(@PathVariable("apiKey") String apiKey, @PathVariable("attractionId") UUID attractionId, @PathVariable("adults") int adults,
                                             @PathVariable("children") int children, @PathVariable("nightsStay") int nightsStay, @PathVariable("rewardsPoints") int rewardsPoints) {
        List<Provider> pricesList = tripPricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        logger.info("Currently creating a list of prices, please wait...");
        ResponseEntity<List<Provider>> pricesResponse = ResponseEntity.status(HttpStatus.OK).body(pricesList);
        logger.info("The following list have been generated: " + pricesList);
        return pricesResponse;
    }
}
