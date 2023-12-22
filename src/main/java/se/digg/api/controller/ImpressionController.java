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
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.exception.DomainValidationException;
import se.digg.api.form.ImpressionForm;
import se.digg.api.logic.ImpressionLogic;
import se.digg.api.response.BadRequestResponse;
import se.digg.api.response.ForbiddenResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@Tag(name = "Impression controller")
@RequestMapping("/impression")
public class ImpressionController {

    private final ImpressionLogic impressionLogic;
    private final HostValidationConfig hostValidationConfig;

    public ImpressionController(ImpressionLogic impressionLogic, HostValidationConfig hostValidationConfig) {
        this.impressionLogic = impressionLogic;
        this.hostValidationConfig = hostValidationConfig;
    }

    @Operation(summary = "On user data UI component presentation send an impression", description = "Given that the client has presented the user data UI component, we expect the component to send an impression. Upon successful transmission of the impression, the client will receive a 200 OK response and a Context ID to be used for rating call.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent the impression", content = @Content(schema = @Schema(implementation = ImpressionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ForbiddenResponse.class))),
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> impression(@Valid @RequestBody ImpressionForm impressionForm, @RequestHeader(name = "X-API-KEY", required = true) String apiKey, HttpServletRequest request) {
        String host = request.getServerName();
        apiKey = apiKey.trim().toLowerCase();

        if (hostValidationConfig.getIsHostCheckingEnabled() && !host.equals(hostValidationConfig.getServerHost())) {
            log.error("CORS - Same origin check failed - {} is not allowed to send impressions!", host);
            throw new DomainValidationException(HttpServletRequest.class, "Same origin check failed", host);
        }

        ImpressionDTO impressionDTO = impressionLogic.saveImpression(impressionForm, apiKey);

        return ResponseEntity.ok(impressionDTO);
    }
}
