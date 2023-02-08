package gps.configModule;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class GpsConfig {
    /**
     * Creating a new util from the librairie, this will be used in service layer
     * @return a new GpsUtil with the good parameters for location of attraction
     */
    @Bean
    public GpsUtil getGpsUtil() {
        Locale.setDefault(Locale.ENGLISH);
        return new GpsUtil();
    }
}
