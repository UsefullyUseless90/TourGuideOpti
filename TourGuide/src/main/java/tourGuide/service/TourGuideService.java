package tourGuide.service;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.model.user.UserPreferences;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardProxy;
import tourGuide.proxies.TripProxy;
import tourGuide.src.main.java.tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.model.user.User;
import tourGuide.model.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TourGuideService {
	private final RewardsService rewardsService;

	//Chunk of the new parts//
	private GpsProxy gpsProxy;
	private RewardProxy rewardProxy;
	private TripProxy tripProxy;
	//*********************//

	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	/**
	 * Logger that's going to inform of the situation on each action
	 */
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	/**
	 * Initialize the TourGuideService with new params
	 * @param gpsProxy
	 * @param rewardsService
	 * @param tripProxy
	 */
	public TourGuideService(GpsProxy gpsProxy, RewardsService rewardsService, TripProxy tripProxy) {
		this.gpsProxy = gpsProxy;
		this.rewardsService = rewardsService;
		this.tripProxy = tripProxy;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/**
	 * This is going to be used to queue tasks, this way process is going to be decomposed in little tasks.
	 * This should have for result a faster execution by not overloading methods with tasks that are added again and again..
	 */
	ThreadPoolExecutor executorService = new ThreadPoolExecutor(1,5,10, TimeUnit.SECONDS,
			new LinkedBlockingDeque<>(3),
			Executors.defaultThreadFactory(),
			new ThreadPoolExecutor.DiscardOldestPolicy()
	);

	/**
	 * Method to return information about the user
	 * @param user
	 * @return a list of rewards own by a specific user
	 */
	public List<UserReward> getUserRewards(User user) {
		logger.info("Rewards for this user: " + user.getUserRewards());
		return user.getUserRewards();
	}

	/**
	 * Method to return information about the user
	 * @param user
	 * @return the user's location within a visited location object
	 */
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
		logger.info(user + "'s infos: " + visitedLocation);
		return visitedLocation;
	}

	/**
	 * Method to find a user
	 * @param userName
	 * @return a user by his username
	 */
	public User getUser(String userName) {
		logger.info(userName + "'s infos: " + internalUserMap.get(userName));
		return internalUserMap.get(userName);
	}
	public UserPreferences setUserPreferences(@RequestParam String username, @RequestBody UserPreferences userPreferences) {
		if(internalUserMap.containsKey(username)){
			User user = getUser(username);
			UserPreferences u2 = new UserPreferences();
			u2.setAttractionProximity(userPreferences.getAttractionProximity());
			u2.setLowerPricePoint(userPreferences.getLowerPricePoint());
			u2.setHighPricePoint(userPreferences.getHighPricePoint());
			u2.setTripDuration(userPreferences.getTripDuration());
			u2.setTicketQuantity(userPreferences.getTicketQuantity());
			u2.setNumberOfAdults(userPreferences.getNumberOfAdults());
			u2.setNumberOfChildren(userPreferences.getNumberOfChildren());
			user.setUserPreferences(u2);
		}else{
			logger.info("No user found!");
		}
		return userPreferences;
	}


	/**
	 * Method to get all users of the system
	 * @return a list of users
	 */
	public List<User> getAllUsers() {
		logger.info("All users: " + internalUserMap.values().stream().collect(Collectors.toList()));
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Method to add a user into the system
	 * @param user
	 */
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
		logger.info(user + " has been successfully added!");
	}

	/**
	 * Method to get trip deals
	 * @param user
	 * @return a list of provider
	 */
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		logger.info("Creating a list of trip deals...");
		user.setTripDeals(providers);
		logger.info("Trip deals available for: " + user);
		logger.info("" + providers);
		return providers;
	}

	/**
	 * Method to get user's id, location and date that the location was visited
	 * @param user
	 * @return Visited Location object
	 */
	public VisitedLocation trackUserLocation(User user) {

		// this is taking too much time to execute //
		//VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());

		//Completable Future, used here for asynchronous reasons, meaning it does not block the principal thread//
		CompletableFuture<VisitedLocation> future = CompletableFuture.supplyAsync(() -> getUserLocation(user), executorService)
				.thenApply(visitedLocation -> {
					user.addToVisitedLocations(visitedLocation);
					rewardsService.calculateRewards(user);
					return visitedLocation;
				});
		VisitedLocation visitedLocation = future.join();
		return visitedLocation;
	}

	/**
	 * We need a base to work on, so we get all the users then we loop through all of them.
	 * With the loop each user's location will be put in a hashmap.
	 * That way we'll have the user's id and his location.
	 *
	 * @return a map of user's id and their specific to each location
	 */
	public Map<String, Location> getAllCurrentLocations() {
		logger.info("Creating a map of all current locations...");
		Map<String, Location> currentLocationMap = new HashMap<>();
		List<User> users = new ArrayList<>();
		logger.info("Creating a list of all users...");
		users = getAllUsers();
		logger.info("Sorting all users by their id & location...");
		for (User user : users){
			currentLocationMap.put(user.getUserId().toString(), user.getLastVisitedLocation().location);
		}
		logger.info("Users information had been put into map.");
		logger.info("Map completed: " + currentLocationMap);
		return currentLocationMap;
	}

	/**
	 * We need all attractions to be put in a list
	 * Then we need to compare the distance between the user and the five closest attractions
	 *
	 * @param visitedLocation
	 * @return a list of the five closest attractions
	 */

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		logger.info("Creating an empty list...");
		List<Attraction> nearbyAttractions = new ArrayList<>();
		logger.info("Creating a list of all attractions...");
		List<Attraction> attractions = gpsProxy.getAttractions();
		logger.info("Sorting attractions list...");
		List<Attraction> closeAttractions = attractions.stream().sorted(Comparator.comparing(attraction -> rewardsService.getDistance(visitedLocation.location, attraction))).collect(Collectors.toList());
		logger.info("Selecting the 5 closest attractions...");
		nearbyAttractions = closeAttractions.subList(0,4);
		logger.info("Result: " + nearbyAttractions);
		return nearbyAttractions;

	}

	/**
	 *
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 *
	 * @param user
	 */
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 *
	 * @return a random longitude
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 *
	 * @return a random latitude
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 *
	 * @return a date
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
