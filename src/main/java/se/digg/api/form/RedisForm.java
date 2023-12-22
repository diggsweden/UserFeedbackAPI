package se.digg.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisForm {

    @NotBlank(message = "Cache key cannot be null or empty")
    String key;

    @NotBlank(message = "Cache value cannot be null or empty")
    String value;
}
