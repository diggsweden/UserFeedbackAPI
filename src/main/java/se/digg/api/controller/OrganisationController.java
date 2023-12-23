// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.controller;

import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.form.RegisterForm;
import se.digg.api.form.UnRegisterForm;
import se.digg.api.logic.OrganisationLogic;
import se.digg.api.response.BadRequestResponse;
import se.digg.api.response.ConflictResponse;
import se.digg.api.response.CustomResponse;

import javax.validation.Valid;

@RestController
@Slf4j
@Tag(name = "Organisation controller")
@RequestMapping("/organisation")
public class OrganisationController {

    private OrganisationLogic organisationLogic;

    @Autowired
    private void setOrganisationLogic(final OrganisationLogic organisationLogic) {
        this.organisationLogic = organisationLogic;
    }

    @Operation(summary = "Register your organisation and receive an access token for the API", description = "Given a unique organisation name and a list of unique domain names from which your service(s) is served from, this endpoint returns an access token to be used when client plugin is interacting with the API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved an access token", content = @Content(schema = @Schema(implementation = ApiKeyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ConflictResponse.class)))
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterForm registerForm) {
        ApiKeyDTO apiKeyDTO = organisationLogic.registerOrganisationAndGetAccessToken(registerForm);

        return ResponseEntity.ok(apiKeyDTO);
    }

    @Operation(summary = "Un-register your organisation via the API", description = "Given an access token coming from the associated organisation's domain this endpoint will remove the organisation's access from the API but re-format the organisations data references to retain the user feedback data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Under construction string returned", content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping(value = "/unregister", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> unregister(@Valid @RequestBody UnRegisterForm unRegisterForm) {
        CustomResponse customResponse = new CustomResponse();
        JsonObject jsonResponse = customResponse.buildResponse(CustomResponse.Status.UNDER_CONSTRUCTION, "This endpoint is under construction");

        // TODO: Implement

        return ResponseEntity.ok(jsonResponse.toString());
    }
}
