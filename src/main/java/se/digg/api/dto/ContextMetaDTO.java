package se.digg.api.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.digg.api.utils.DateFormats;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextMetaDTO {

    private Long id;
    private Long contextId;
    private String value;
    private String type;
    private Date createdAt;
    private Date updatedAt;

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("context_id", this.contextId);
        jsonObject.addProperty("value", this.value);
        jsonObject.addProperty("type", this.type);
        jsonObject.addProperty("created_at", DateFormats.getDateTimeString(this.createdAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        jsonObject.addProperty("updated_at", DateFormats.getDateTimeString(this.updatedAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        return jsonObject;
    }
}
