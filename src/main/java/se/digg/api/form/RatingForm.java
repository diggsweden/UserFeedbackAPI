// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingForm {

    @NotNull(message = "Impression ID cannot be null or empty")
    Long impressionId;

    @NotBlank(message = "Fingerprint cannot be null or empty")
    String fingerprint;

    @NotNull(message = "Score cannot be null or empty")
    @Min(value = 1, message = "Score should be greater or equal to 1")
    @Max(value = 5, message = "Score should be less then or equal to 5")
    Integer score;
}
