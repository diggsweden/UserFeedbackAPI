// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImpressionForm {

    @NotBlank(message = "Fingerprint cannot be null or empty")
    String fingerprint;

    @NotBlank(message = "Domain cannot be null or empty")
    String domain;

    @NotBlank(message = "Path cannot be null or empty")
    String path;

    @NotBlank(message = "Name cannot be null or empty")
    String name;

    String htmlId;

    List<String> tags;

    List<String> labels;
}
