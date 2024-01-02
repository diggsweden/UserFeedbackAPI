// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.response;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomResponse {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String UNDER_CONSTRUCTION = "UNDER_CONSTRUCTION";

    public enum Status {
        SUCCESS,
        FAILURE,
        UNDER_CONSTRUCTION
    }

    String status;
    String message;

    public JsonObject buildResponse(Status status, String message) {
        JsonObject jsonReponse = new JsonObject();

        switch (status) {
            case SUCCESS:
                jsonReponse.addProperty("status", SUCCESS);
                break;
            case FAILURE:
                jsonReponse.addProperty("status", FAILURE);
                break;
            case UNDER_CONSTRUCTION:
                jsonReponse.addProperty("status", UNDER_CONSTRUCTION);
                break;
        }

        jsonReponse.addProperty("message", message);

        return jsonReponse;
    }
}
