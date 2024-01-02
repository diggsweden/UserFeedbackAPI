// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.digg.api.error.ApiSubError;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ForbiddenResponse {
    @Schema(name = "status", example = "FORBIDDEN")
    String status;
    List<Integer> timestamp = new ArrayList<>() {
        {
            add(2023);
            add(8);
            add(25);
            add(15);
            add(18);
            add(35);
            add(642718107);
        }
    };
    String message;
    String debugMessage;
    List<ApiSubError> subErrors = new ArrayList<>();
}
