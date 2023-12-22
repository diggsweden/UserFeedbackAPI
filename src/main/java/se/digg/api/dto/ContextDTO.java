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
public class ContextDTO {

    private Long id;
    private Long organisationId;
    private Long domainId;
    private String path;
    private String name;
    private String htmlId;
    private Date createdAt;
    private Date updatedAt;

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("organisation_id", this.organisationId);
        jsonObject.addProperty("domain", this.domainId);
        jsonObject.addProperty("path", this.path);
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("html_id", this.htmlId);
        jsonObject.addProperty("created_at", DateFormats.getDateTimeString(this.createdAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        jsonObject.addProperty("updated_at", DateFormats.getDateTimeString(this.updatedAt, DateFormats.STANDARD_DATE_TIME_FORMAT));
        return jsonObject;
    }
}
