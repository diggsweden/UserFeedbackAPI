package se.digg.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.digg.api.config.HostValidationConfig;
import se.digg.api.dto.RatingDTO;
import se.digg.api.error.ApiError;
import se.digg.api.exception.DomainValidationException;
import se.digg.api.form.RatingForm;
import se.digg.api.logic.RatingLogic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@Tag(name = "Rating controller")
@RequestMapping("/rating")
public class RatingController {

    private final RatingLogic ratingLogic;
    private final HostValidationConfig hostValidationConfig;

    public RatingController(RatingLogic ratingLogic, HostValidationConfig hostValidationConfig) {
        this.ratingLogic = ratingLogic;
        this.hostValidationConfig = hostValidationConfig;
    }

    @Operation(summary = "On user data UI component presentation send an impression", description = "Given that the client has has presented the user data UI component, we expect the component to send an impression. Upon successful transmission of the impression, the client will receive a 200 OK response and a Context ID to be used for rating call.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent the impression", content = @Content(schema = @Schema(implementation = RatingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> impression(@Valid @RequestBody RatingForm ratingForm, @RequestHeader(name = "X-API-KEY", required = true) String apiKey, HttpServletRequest request) {
        String host = request.getServerName();
        apiKey = apiKey.trim().toLowerCase();

        if (hostValidationConfig.getIsHostCheckingEnabled() && !host.equals(hostValidationConfig.getServerHost())) {
            log.error("CORS - Same origin check failed - {} is not allowed to send ratings!", host);
            throw new DomainValidationException(HttpServletRequest.class, "Same origin check failed", host);
        }

        RatingDTO ratingDTO = ratingLogic.saveRating(ratingForm, apiKey);

        return ResponseEntity.ok(ratingDTO);
    }
}
