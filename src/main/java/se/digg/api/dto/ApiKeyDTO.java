package se.digg.api.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.digg.api.utils.DateFormats;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyDTO {
    private Long id;
    private Long organisationId;
    private String apiKey;
    private Date createdAt;
    private Date updatedAt;

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("organisation_id", this.organisationId);
        jsonObject.addProperty("api_key", this.apiKey);
        jsonObject.addProperty("created_at", DateFormats.getDateTimeString(this.createdAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        jsonObject.addProperty("updated_at", DateFormats.getDateTimeString(this.updatedAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        return jsonObject;
    }
}
