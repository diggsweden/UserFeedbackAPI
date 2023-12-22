package se.digg.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnRegisterForm {

    @NotBlank(message = "Access token cannot be null or empty")
    String accessToken;
}
