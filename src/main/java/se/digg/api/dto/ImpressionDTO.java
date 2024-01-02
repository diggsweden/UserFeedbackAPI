// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.digg.api.utils.DateFormats;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImpressionDTO {

    private Long id;
    private Long contextId;
    private String fingerprint;
    private BigDecimal avgRating;
    private Integer ratingCount;
    private Date createdAt;
    private Date updatedAt;

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("context_id", this.contextId);
        jsonObject.addProperty("fingerprint", this.fingerprint);
        jsonObject.addProperty("avg_rating", this.avgRating);
        jsonObject.addProperty("rating_count", this.ratingCount);
        jsonObject.addProperty("created_at", DateFormats.getDateTimeString(this.createdAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        jsonObject.addProperty("updated_at", DateFormats.getDateTimeString(this.updatedAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        return jsonObject;
    }
}
