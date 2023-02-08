package reward.configModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class RewardConfig {
    /**
     * Creating a new util from the librairie, this will be used in service layer
     * @return a new RewardCentral
     */
    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }
}
