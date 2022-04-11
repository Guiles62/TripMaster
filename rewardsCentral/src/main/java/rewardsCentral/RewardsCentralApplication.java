package rewardsCentral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class RewardsCentralApplication {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SpringApplication.run(RewardsCentralApplication.class, args);
    }
}
