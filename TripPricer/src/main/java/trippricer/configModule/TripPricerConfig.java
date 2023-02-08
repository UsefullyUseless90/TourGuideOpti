package trippricer.configModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

@Configuration
public class TripPricerConfig {
    /**
     * Creating a new util from the librairie, this will be used in service layer
     * @return a new TripPricer
     */
    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }
}
