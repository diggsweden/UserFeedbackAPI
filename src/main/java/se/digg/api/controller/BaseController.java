package se.digg.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "Base controller - Collect user ratings for a given organisations services and at certain points of the user's journey.")
public class BaseController {

    // Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Value("${spring.application.msg.welcome}")
    String welcomeMessage;

    @Operation(summary = "Base path for the API", description = "Returns a welcome message from the API")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String welcome() {
        return welcomeMessage;
    }
}
