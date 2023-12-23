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
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "context_id", nullable = false)
    private Long contextId;

    @Column(name = "impression_id", nullable = false, unique = true)
    private Long impressionId;

    @Column(name = "fingerprint", nullable = false)
    private String fingerprint;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = DateFormats.STANDARD_DATE_TIME_FORMAT)
    private Date updatedAt;

    public Rating(Long contextId, Long impressionId, String fingerprint, Integer score) {
        this.contextId = contextId;
        this.impressionId = impressionId;
        this.fingerprint = fingerprint;
        this.score = score;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}

