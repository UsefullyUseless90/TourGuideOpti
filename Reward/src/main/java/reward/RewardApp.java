package reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Collections;

@SpringBootApplication
@EnableFeignClients
public class RewardApp {
    //Api won't listen to the application.properties
    //that's why the main method looks like this.
    public static void main(String[] args) {
        SpringApplication rewardApp = new SpringApplication(RewardApp.class);
        rewardApp.setDefaultProperties(Collections.singletonMap("server.port", "8088"));
        rewardApp.run(args);
        //SpringApplication.run(RewardApp.class, args);
    }
}
