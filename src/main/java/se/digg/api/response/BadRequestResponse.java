package se.digg.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import se.digg.api.error.ApiSubError;

import java.util.ArrayList;
import java.util.List;

@Data
public class BadRequestResponse {
    @Schema(name = "status", example = "BAD_REQUEST")
    String status = "BAD_REQUEST";
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
    String message = "string";
    String debugMessage = "string";
    List<ApiSubError> subErrors = new ArrayList<>();
}
