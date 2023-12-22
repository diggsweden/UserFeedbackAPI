package se.digg.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationProperties(prefix = "impression")
@EnableConfigurationProperties
@Configuration
@Getter
@Setter
public class HostValidationConfig implements WebMvcConfigurer {
    private Boolean isHostCheckingEnabled;
    private String serverHost;
}