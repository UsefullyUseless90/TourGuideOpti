package tourGuide.configurationModule;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardProxy;
import tourGuide.service.RewardsService;

import java.util.Locale;

@Configuration
@ComponentScan
public class TourGuideModule {

	/**
	 * Reward proxy is going to be the link between Reward Api and TourGuide App
	 */
	RewardProxy rewardProxy;
	/**
	 * Gps proxy is going to be the link between GPS Api and TourGuide App
	 */
	GpsProxy gpsProxy;

	/**
	 * Creating a new util from the librairie, this will be used in service layer
	 * @return a new GpsUtil with the good parameters for location of attraction
	 */
	@Bean
	public GpsUtil getGpsUtil() {
		Locale.setDefault(Locale.ENGLISH);
		return new GpsUtil();
	}

	/**
	 * Creating a new util from the librairie, this will be used in service layer
	 * @return a new RewardCentral
	 */
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	/**
	 * Creating a new service layer using new params
	 * @return a parametrized RewardService using proxies
	 */
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(gpsProxy, rewardProxy);
	}
}
