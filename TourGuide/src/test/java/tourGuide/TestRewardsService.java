package tourGuide;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.model.user.User;
import tourGuide.model.user.UserReward;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardProxy;
import tourGuide.proxies.TripProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.src.main.java.tourGuide.helper.InternalTestHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRewardsService {

	@Autowired
	private GpsProxy gpsProxy;
	@Autowired
	private RewardProxy rewardsProxy;
	@Autowired
	private TripProxy tripProxy;


	@Test
	public void userGetRewards() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsProxy);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripProxy);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsProxy.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsProxy);
		Attraction attraction = gpsProxy.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		//GpsUtil gpsUtil = new GpsUtil();
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Date date = new Date();
		Double latitude = 0.26;
		Double longitude = 0.26;
		Location location = new Location(longitude, latitude);
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),location,date);
		Attraction attraction = new Attraction("none","city","state",latitude, longitude);
		UserReward userReward = new UserReward(visitedLocation, attraction);
		user.addUserReward(userReward);

		RewardsService rewardsService = new RewardsService(gpsProxy, rewardsProxy);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripProxy);

		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = tourGuideService.getUserRewards(user);
		tourGuideService.tracker.stopTracking();

		assertEquals(1, userRewards.size());

	}
	
}
