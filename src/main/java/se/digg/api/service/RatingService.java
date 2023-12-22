package se.digg.api.service;

import org.javatuples.Pair;
import se.digg.api.dto.RatingDTO;
import se.digg.api.model.Rating;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RatingService {

    Rating save(Rating rating);

    List<Rating> findAll();

    Rating findById(Long id);

    List<Rating> findByContextId(Long contextId);

    Pair<BigDecimal, Integer> getAverageRatingInfoByContextId(Long contextId);

    List<Rating> findByFingerprint(String fingerprint);

    Optional<Rating> findByImpressionId(Long impressionId);

    Rating update(Rating rating);

    void delete(Long id);

    void deleteAll();

    Rating getEntityFromDTO(RatingDTO ratingDTO);

    RatingDTO getDTOFromEntity(Rating rating);
}
