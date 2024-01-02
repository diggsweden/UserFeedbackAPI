// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import se.digg.api.utils.DateFormats;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "apikey")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organisation_id", nullable = false, unique = true)
    private Long organisationId;

    @Column(name = "uuid4", nullable = false, unique = true)
    private String uuid4;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date updatedAt;

    public ApiKey(Long organisationId, String uuid4) {
        this.organisationId = organisationId;
        this.uuid4 = uuid4;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public ApiKey(Long organisationId) {
        this.organisationId = organisationId;
        this.uuid4 = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
