package tourGuide.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.controller.TourGuideController;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardProxy;
import tourGuide.model.user.User;
import tourGuide.model.user.UserReward;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RewardsService {

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;

	private final RewardProxy rewardProxy;
	private final GpsProxy gpsProxy;

	private final Logger logger = LogManager.getLogger(TourGuideController.class);

	/**
	 * Initialize Reward Service with new params
	 * @param gpsProxy
	 * @param rewardProxy
	 */
	public RewardsService(GpsProxy gpsProxy, RewardProxy rewardProxy) {
		this.gpsProxy = gpsProxy;
		this.rewardProxy = rewardProxy;
	}

	/**
	 *
	 * @param proximityBuffer
	 */
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	/**
	 *
	 */
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Method that calculate rewards for a user
	 * @param user
	 * @return user
	 */
	public CompletableFuture<User> calculateRewards(User user) {
		logger.info("Creating a list of all visited location by :" + user);
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		logger.info("Creating a list of all attractions...");
		List<Attraction> attractions = gpsProxy.getAttractions();
		logger.info("Seeking visited location by user...");
		for(VisitedLocation visitedLocation : userLocations) {
			logger.info("Seeking attractions done by user...");
			for(Attraction attraction : attractions) {
				logger.info("Sorting rewards for attraction done by user...");
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						logger.info("Adding rewards for new visited attractions user...");
						user.addUserReward(new UserReward(visitedLocation, attraction));
					}
				}
			}
		}
		logger.info("Rewards are up to date for: " + user);
		return CompletableFuture.completedFuture(user) ;
	}

	/**
	 * Method used to know if an attraction is in proximity or not
	 * @param attraction
	 * @param location
	 * @return boolean
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	/**
	 * Method to get attraction near last location
	 * @param visitedLocation
	 * @param attraction
	 * @return distance between attraction and last location
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	/*
	private CompletableFuture<Integer> getRewardPoints(Attraction attraction, User user) {
		return rewardProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	 */

	/**
	 * Method used for distance in miles
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
