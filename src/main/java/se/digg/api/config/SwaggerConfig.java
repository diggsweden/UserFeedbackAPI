package se.digg.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI security() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SecurityScheme.Type.APIKEY.toString(), new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .name("X-API-KEY")
                                .in(SecurityScheme.In.HEADER)
                                .description("API key for the organisation. <br /><br />**Please note:** that the API key needs to be inserted manually in the requests when utilizing the Swagger UI.<br /><br />")))
                .info(new Info().title("UserRating API")
                        .description("This is the main API for the user rating data collection.")
                        .version("v1.0.0"));
    }
}
