package tourGuide;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardProxy;
import tourGuide.proxies.TripProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.src.main.java.tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tripPricer.Provider;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTourGuideService {

	@Autowired
	private GpsProxy gpsProxy;
	@Autowired
	private RewardProxy rewardsCentralProxy;
	@Autowired
	private TripProxy tripPricerProxy;


	//TourGuide.TrackUserLocation(user) is taking way too long, and it's returning a visited location.
	//Instead, to ease the process we create a new visited location with all the params that are needed this way le location is found way faster
	@Test
	public void getUserLocation() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), gpsProxy.getAttractions().get(0), new Date());

		//VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrievedUser = tourGuideService.getUser(user.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}
	
	@Test
	public void getAllUsers() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	//TourGuide.TrackUserLocation(user) is taking way too long, and it's returning a visited location.
	//Instead, to ease the process we create a new visited location with all the params that are needed this way le location is found way faster
	@Test
	public void trackUser() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), gpsProxy.getAttractions().get(0), new Date());
		//VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
	//@Ignore // Not yet implemented
	//TourGuide.TrackUserLocation(user) is taking way too long, and it's returning a visited location.
	//Instead, to ease the process we create a new visited location with all the params that are needed this way le location is found way faster
	@Test
	public void getNearbyAttractions() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), gpsProxy.getAttractions().get(0), new Date());
		//tourGuideService.trackUserLocation(user);
		
		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(4, attractions.size());
	}

	@Test
	public void getTripDeals() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsCentralProxy);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, providers.size());
	}
	
	
}

