package se.digg.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import se.digg.api.utils.DateFormats;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "context_meta")
public class ContextMeta {

    public enum Type {
        LABEL,
        TAG
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "context_id", nullable = false)
    private Long contextId;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date updatedAt;

    public ContextMeta(Long contextId, String value, ContextMeta.Type type) {
        this.contextId = contextId;
        this.value = value;
        this.type = type;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}

