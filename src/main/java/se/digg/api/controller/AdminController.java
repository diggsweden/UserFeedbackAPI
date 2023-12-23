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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.digg.api.logic.AdminLogic;
import se.digg.api.response.CustomResponse;

@RestController
@Slf4j
@Tag(name = "Admin controller")
@RequestMapping("/admin")
public class AdminController {

    private AdminLogic adminLogic;

    @Autowired
    private void setAdminLogic(final AdminLogic adminLogic) {
        this.adminLogic = adminLogic;
    }

    @Operation(summary = "Clear all data", description = "Given access upon invocation this endpoint will clear all data in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All data has been cleared from the database", content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping(value = "/cleardb", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clearDb() {
        CustomResponse customResponse = new CustomResponse();
        JsonObject jsonResponse = customResponse.buildResponse(CustomResponse.Status.SUCCESS, "All data has been cleared from the database");

        adminLogic.clearAllData();

        return ResponseEntity.ok(jsonResponse.toString());
    }
}
