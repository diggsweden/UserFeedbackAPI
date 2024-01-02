// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.service;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import se.digg.api.config.RatingConfig;
import se.digg.api.dto.RatingDTO;
import se.digg.api.model.Rating;
import se.digg.api.repository.RatingRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.StreamSupport;


@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final RatingConfig ratingConfig;

    public RatingServiceImpl(final RatingRepository ratingRepository, final RatingConfig ratingConfig) {
        this.ratingRepository = ratingRepository;
        this.ratingConfig = ratingConfig;
    }

    public Rating save(Rating Rating) throws IllegalArgumentException {
        return ratingRepository.save(Rating);
    }

    public List<Rating> findAll() {
        Iterator<Rating> RatingIterator = ratingRepository.findAll().iterator();
        List<Rating> RatingList = new ArrayList<>();

        while (RatingIterator.hasNext()) {
            Rating Rating = RatingIterator.next();
            RatingList.add(Rating);
        }

        return RatingList;
    }

    public Rating findById(Long id) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        return optionalRating.orElseThrow(() -> new IllegalArgumentException("Rating not found"));
    }

    public List<Rating> findByContextId(Long contextId) {
        return ratingRepository.findByContextId(contextId);
    }

    public Optional<Rating> findByImpressionId(Long impressionId) {
        return ratingRepository.findByImpressionId(impressionId);
    }

    public Pair<BigDecimal, Integer> getAverageRatingInfoByContextId(Long contextId) {
        Timestamp ratingMaxAge = Timestamp.from(ZonedDateTime.now().minusSeconds(ratingConfig.getExpirationTimeInSeconds()).toInstant());
        Iterable<Rating> ratingsIterable = ratingRepository.findByContextIdAndCreatedAtAfter(contextId, ratingMaxAge);

        List<Rating> ratings = StreamSupport.stream(ratingsIterable.spliterator(), false).toList();

        if (ratings.isEmpty()) {
            return new Pair<BigDecimal, Integer>(new BigDecimal("0.0"), 0);
        }

        OptionalDouble optionalAverage = ratings.stream().mapToDouble(Rating::getScore).average();

        if (optionalAverage.isEmpty()) {
            return new Pair<BigDecimal, Integer>(new BigDecimal("0.0"), 0);
        }

        BigDecimal average = BigDecimal.valueOf(optionalAverage.getAsDouble());
        return new Pair<BigDecimal, Integer>(average, ratings.size());
    }

    public List<Rating> findByFingerprint(String fingerprint) {
        return ratingRepository.findByFingerprint(fingerprint);
    }

    public Rating update(Rating Rating) {
        return ratingRepository.save(Rating);
    }

    public void delete(Long id) {
        ratingRepository.deleteById(id);
    }

    public void deleteAll() {
        ratingRepository.deleteAll();
    }

    public Rating getEntityFromDTO(RatingDTO ratingDTO) {
        return new Rating(ratingDTO.getContextId(), ratingDTO.getImpressionId(), ratingDTO.getFingerprint(), ratingDTO.getScore());
    }

    public RatingDTO getDTOFromEntity(Rating Rating) {
        return new RatingDTO(Rating.getId(),
                Rating.getContextId(),
                Rating.getImpressionId(),
                Rating.getFingerprint(),
                Rating.getScore(),
                Rating.getCreatedAt(),
                Rating.getUpdatedAt()
        );
    }
}
