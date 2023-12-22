package se.digg.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.Rating;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {

    List<Rating> findByContextId(Long contextId);

    @Query("SELECT rating FROM Rating rating WHERE rating.contextId = ?1 AND rating.createdAt >= ?2")
    Iterable<Rating> findByContextIdAndCreatedAtAfter(Long contextId, Timestamp ratingMaxAge);

    List<Rating> findByFingerprint(String fingerprint);

    Optional<Rating> findByImpressionId(Long impressionId);
}
