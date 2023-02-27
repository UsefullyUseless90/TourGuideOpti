package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.model.user.UserPreferences;
import tourGuide.service.TourGuideService;
import tourGuide.model.user.User;
import tripPricer.Provider;

import java.util.List;
import java.util.Map;

@RestController
public class TourGuideController {

	@Autowired
    TourGuideService tourGuideService;
    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(TourGuideController.class);

    /**
     * Greeting screen from the API
     * @return string
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * Find the location of a user using his/her username
     * @param userName
     * @return VisitedLocation object into a json format
     */
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        logger.info(userName + " is " + visitedLocation);
		return JsonStream.serialize(visitedLocation.location);
    }

    /**
     * Endpoint to get a list of the five closest attractions
     * Username is used to locate the user then with the location we define 5 closest attractions
     * @param userName
     * @return  a list containing the 5 closest attractions object into a json
     */
    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        logger.info(userName + "this the 5 closest attraction: " + visitedLocation);
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }

    /**
     * Endpoint to get all rewards of a user, using his username
     * @param userName
     * @return a list of UserReward object into a json format
     */
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
        logger.info("This the rewards for: " + userName + tourGuideService.getUserRewards(getUser(userName)));
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * Endpoint for all users current location, giving the location of each user and their ids
     * @return a map of location object and strings into a json format
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        Map<String, Location> allCurrentLocations = tourGuideService.getAllCurrentLocations();
        logger.info("This is all the current location: " + allCurrentLocations);
        return JsonStream.serialize(allCurrentLocations);
    }

    /**
     * Endpoint for trip deals according to username
     * @param userName
     * @return a list of providers object into a json format
     */
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        logger.info("This is all the trip deals" + providers + "for this user: " + userName);
    	return JsonStream.serialize(providers);
    }

    /**
     * Endpoint to get a user by his username
     * @param userName
     * @return the user
     */
    private User getUser(String userName) {
        logger.info("Seeking for the user: " + userName);
        logger.info("User found: " + tourGuideService.getUser(userName));
    	return tourGuideService.getUser(userName);
    }
    @PutMapping("/setUserPreferences")
    public String setUserPreferences(@RequestParam String username, @RequestBody UserPreferences userPreferences) {
        return JsonStream.serialize(tourGuideService.setUserPreferences(username, userPreferences));
    }

}