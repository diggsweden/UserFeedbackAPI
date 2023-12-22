package se.digg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class UserFeedbackApiApplication {
    private static final String welcomeMsg = "Welcome to the User-rating API!";
    private static final Logger log = LoggerFactory.getLogger(UserFeedbackApiApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(UserFeedbackApiApplication.class, args);
        ConfigurableEnvironment env = ctx.getEnvironment();

        log.info(env.getProperty("spring.datasource.url"));
        log.info(welcomeMsg);
    }
}
