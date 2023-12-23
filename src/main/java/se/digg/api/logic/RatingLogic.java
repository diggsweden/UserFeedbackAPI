// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.logic;

import org.springframework.stereotype.Component;
import se.digg.api.dto.ContextDTO;
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.dto.RatingDTO;
import se.digg.api.exception.GeneralRuntimeException;
import se.digg.api.form.RatingForm;
import se.digg.api.model.Rating;
import se.digg.api.service.ContextService;
import se.digg.api.service.ImpressionService;
import se.digg.api.service.RatingService;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Component
public class RatingLogic {
    private final OrganisationLogic organisationLogic;
    private final RedisCacheLogic redisCacheLogic;
    private final ImpressionService impressionService;
    private final RatingService ratingService;
    private final ContextService contextService;

    public RatingLogic(final OrganisationLogic organisationLogic, final RedisCacheLogic redisCacheLogic, final ImpressionService impressionService, final RatingService ratingService, final ContextService contextService) {
        this.organisationLogic = organisationLogic;
        this.redisCacheLogic = redisCacheLogic;
        this.impressionService = impressionService;
        this.ratingService = ratingService;
        this.contextService = contextService;
    }

    @Transactional
    public RatingDTO saveRating(RatingForm ratingForm, String apiKey) {
        Long organisationId = organisationLogic.getOrganisationIdByApiKeyUuid4(apiKey);
        ImpressionDTO impressionDTO = impressionService.getDTOFromEntity(impressionService.findById(ratingForm.getImpressionId()));
        ContextDTO contextDTO = contextService.getDTOFromEntity(contextService.findById(impressionDTO.getContextId()));

        if (!Objects.equals(contextDTO.getOrganisationId(), organisationId)) {
            throw new GeneralRuntimeException(RatingForm.class, "Rating does not belong to associated context ID", ratingForm.getImpressionId().toString());
        }

        String ratingCacheKey = generateRatingCacheKey(ratingForm, organisationId);

        // Check cache for a similar rating from the same fingerprint (anonymous user)
        Object ratingCacheKeyValue = redisCacheLogic.get(ratingCacheKey);

        if (ratingCacheKeyValue != null) {
            Long ratingId = Long.parseLong(ratingCacheKeyValue.toString());
            return ratingService.getDTOFromEntity(ratingService.findById(ratingId));
        }

        // Lookup rating by impressionId
        Optional<Rating> optionalPreviousRating = ratingService.findByImpressionId(ratingForm.getImpressionId());
        RatingDTO previousRatingDTO = optionalPreviousRating.map(ratingService::getDTOFromEntity).orElse(null);

        if (previousRatingDTO != null) {
            return previousRatingDTO;
        }

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setImpressionId(impressionDTO.getId());
        ratingDTO.setContextId(contextDTO.getId());
        ratingDTO.setScore(ratingForm.getScore());
        ratingDTO.setFingerprint(ratingForm.getFingerprint());

        Rating rating = ratingService.save(ratingService.getEntityFromDTO(ratingDTO));
        RatingDTO newRatingDTO = ratingService.getDTOFromEntity(rating);

        // Cache rating
        redisCacheLogic.set(ratingCacheKey, newRatingDTO.getId());

        return newRatingDTO;
    }

    private String generateRatingCacheKey(RatingForm ratingForm, Long organisationId) {
        String content = String.join(":", ratingForm.getFingerprint(), ratingForm.getImpressionId().toString(), organisationId.toString());
        return redisCacheLogic.getBase64dHash(content);
    }
}
