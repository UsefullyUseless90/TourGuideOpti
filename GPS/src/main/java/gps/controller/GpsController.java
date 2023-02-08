package gps.controller;

import gps.service.IgpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    @Autowired
    private IgpsService igpsService;

    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(GpsController.class);

    /**
     * Welcome screen of the GPS Api
     * @return a greeting message
     */
    @RequestMapping("/gps")
    public String index() {
        return "This is the tour guide gps, how can we help you?";
    }

    /**
     * Endpoint to get a user's location
     * @param userId
     * @return the location of a user using his id
     */
    @RequestMapping("/getUserLocation")
    public ResponseEntity<?> getLocation(@RequestParam UUID userId){
        logger.info("Currently looking for user n°" + userId + "...");
        VisitedLocation visitedLocation = igpsService.getUserLocation(userId);
        ResponseEntity<?> location = ResponseEntity.status(HttpStatus.OK).body(visitedLocation);
        logger.info("Result: " + visitedLocation);
        return location;
    }

    /**
     * Endpoint to get a list of attractions
     * @return a list of attractions
     */
    @RequestMapping("/getAttractions")
    public ResponseEntity<?> getAttractions() {
        List<Attraction> attractionList = igpsService.getAttractions();
        logger.info("Currently creating a list of attractions, please wait...");
        ResponseEntity<?> attractionResponse = ResponseEntity.status(HttpStatus.OK).body(attractionList);
        logger.info("The following list have been generated: " + attractionList);
        return attractionResponse;
    }

}
