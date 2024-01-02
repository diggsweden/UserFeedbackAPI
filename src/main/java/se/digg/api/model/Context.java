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


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "context")
public class Context {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organisation_id", nullable = false)
    private Long organisationId;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "htmlId")
    private String htmlId;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date updatedAt;

    public Context(Long organisationId, Long domainId, String path, String name, String htmlId) {
        this.organisationId = organisationId;
        this.domainId = domainId;
        this.path = path;
        this.name = name;
        this.htmlId = htmlId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}

