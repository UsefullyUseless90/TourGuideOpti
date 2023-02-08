package tourGuide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("tourGuide.proxies")
public class tourGuide {

    public static void main(String[] args) {
        SpringApplication.run(tourGuide.class, args);
    }

}
