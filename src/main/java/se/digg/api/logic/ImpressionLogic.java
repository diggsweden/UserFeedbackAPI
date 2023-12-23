// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.logic;

import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import se.digg.api.config.ImpressionConfig;
import se.digg.api.dto.ContextDTO;
import se.digg.api.dto.ContextMetaDTO;
import se.digg.api.dto.DomainDTO;
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.exception.GeneralRuntimeException;
import se.digg.api.form.ImpressionForm;
import se.digg.api.model.Context;
import se.digg.api.model.ContextMeta;
import se.digg.api.model.Impression;
import se.digg.api.service.ContextMetaService;
import se.digg.api.service.ContextService;
import se.digg.api.service.ImpressionService;
import se.digg.api.service.RatingService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ImpressionLogic {
    private final OrganisationLogic organisationLogic;
    private final RedisCacheLogic redisCacheLogic;
    private final RatingService ratingService;
    private final ContextService contextService;
    private final ContextMetaService contextMetaService;
    private final ImpressionService impressionService;
    private final ImpressionConfig impressionConfig;

    public ImpressionLogic(final OrganisationLogic organisationLogic, final RedisCacheLogic redisCacheLogic, final RatingService ratingService, final ContextService contextService, final ContextMetaService contextMetaService, final ImpressionService impressionService, final ImpressionConfig impressionConfig) {
        this.organisationLogic = organisationLogic;
        this.redisCacheLogic = redisCacheLogic;
        this.ratingService = ratingService;
        this.contextService = contextService;
        this.contextMetaService = contextMetaService;
        this.impressionService = impressionService;
        this.impressionConfig = impressionConfig;
    }

    @Transactional
    public ImpressionDTO saveImpression(ImpressionForm impressionForm, String apiKey) throws IllegalArgumentException {
        Long organisationId = organisationLogic.getOrganisationIdByApiKeyUuid4(apiKey);
        DomainDTO domainDTO = organisationLogic.getDomainByDomainName(impressionForm.getDomain());
        String fingerprint = impressionForm.getFingerprint();


        if (!Objects.equals(domainDTO.getOrganisationId(), organisationId)) {
            throw new GeneralRuntimeException(ImpressionForm.class, "Domain does not belong to associated organisation", domainDTO.getName());
        }

        String contextCacheKey = generateContextCacheKey(impressionForm, organisationId, domainDTO.getId());

        // Check cache for a similar context from the same fingerprint(anonymous user)
        String contextAndFingerprintCacheKey = contextCacheKey + ":" + fingerprint;
        Object contextAndFingerprintCacheKeyValue = redisCacheLogic.get(contextAndFingerprintCacheKey);

        if (contextAndFingerprintCacheKeyValue != null) {
            Long impressionId = Long.parseLong(contextAndFingerprintCacheKeyValue.toString());
            ImpressionDTO previousImpressionDTO = getImpressionById(impressionId);

            // Check previous impression age has exceeded threshold, then allow new impression
            Date createdAt = previousImpressionDTO.getCreatedAt();
            ZonedDateTime impressionMaxAge = ZonedDateTime.now().minusSeconds(impressionConfig.getExpirationTimeInSeconds());

            // Early return - on spam or double post or too early to send an impression again - response is partially idempotent
            if (!createdAt.toInstant().isBefore(impressionMaxAge.toInstant())) {

                // Calculate average rating for context and rating count
                Pair<BigDecimal, Integer> avgRatingAndRatingCount = ratingService.getAverageRatingInfoByContextId(previousImpressionDTO.getContextId());
                previousImpressionDTO.setAvgRating(avgRatingAndRatingCount.getValue0());
                previousImpressionDTO.setRatingCount(avgRatingAndRatingCount.getValue1());

                return previousImpressionDTO;
            }
        }

        Optional<Long> originalContextId = Optional.empty();

        // Check cache for a similar context
        Object contextCacheKeyValue = redisCacheLogic.get(contextCacheKey);

        if (contextCacheKeyValue != null) {
            Long contextId = Long.parseLong(contextCacheKeyValue.toString());
            originalContextId = Optional.of(contextId);

        } else {

            // If cache miss check in db if context already exists
            originalContextId = lookupContextByImpressionFormValues(
                    impressionForm.getPath(),
                    impressionForm.getName(),
                    impressionForm.getHtmlId(),
                    organisationId,
                    domainDTO.getId(),
                    impressionForm.getTags(),
                    impressionForm.getLabels());
        }

        // If context is not present, create a new one
        Optional<Long> contextId = Optional.empty();
        if (originalContextId.isEmpty()) {
            ContextDTO contextDTO = new ContextDTO();
            contextDTO.setPath(impressionForm.getPath());
            contextDTO.setName(impressionForm.getName());
            contextDTO.setHtmlId(impressionForm.getHtmlId());
            contextDTO.setOrganisationId(organisationId);
            contextDTO.setDomainId(domainDTO.getId());

            Context newContext = contextService.save(contextService.getEntityFromDTO(contextDTO));

            if (impressionForm.getTags() != null) {
                for (String tags : impressionForm.getTags()) {
                    ContextMetaDTO contextMetaDTO = new ContextMetaDTO();
                    contextMetaDTO.setContextId(newContext.getId());
                    contextMetaDTO.setValue(tags);
                    contextMetaDTO.setType(ContextMeta.Type.TAG.toString());
                    contextMetaService.save(contextMetaService.getEntityFromDTO(contextMetaDTO));
                }
            }

            if (impressionForm.getLabels() != null) {
                for (String label : impressionForm.getLabels()) {
                    ContextMetaDTO contextMetaDTO = new ContextMetaDTO();
                    contextMetaDTO.setContextId(newContext.getId());
                    contextMetaDTO.setValue(label);
                    contextMetaDTO.setType(ContextMeta.Type.LABEL.toString());
                    contextMetaService.save(contextMetaService.getEntityFromDTO(contextMetaDTO));
                }
            }

            contextId = Optional.of(newContext.getId());
        }

        ImpressionDTO impressionDTO = new ImpressionDTO();
        contextId.ifPresent(impressionDTO::setContextId);
        originalContextId.ifPresent(impressionDTO::setContextId);
        impressionDTO.setFingerprint(fingerprint);

        Impression newImpression = impressionService.save(impressionService.getEntityFromDTO(impressionDTO));
        ImpressionDTO newImpressionDTO = impressionService.getDTOFromEntity(newImpression);

        // Calculate average rating for context and rating count
        Pair<BigDecimal, Integer> avgRatingAndRatingCount = ratingService.getAverageRatingInfoByContextId(impressionDTO.getContextId());
        newImpressionDTO.setAvgRating(avgRatingAndRatingCount.getValue0());
        newImpressionDTO.setRatingCount(avgRatingAndRatingCount.getValue1());

        redisCacheLogic.set(contextAndFingerprintCacheKey, newImpressionDTO.getId());
        redisCacheLogic.set(contextCacheKey, newImpressionDTO.getContextId());

        return newImpressionDTO;
    }

    public ImpressionDTO getImpressionById(Long impressionId) {
        Impression impression = impressionService.findById(impressionId);
        return impressionService.getDTOFromEntity(impression);
    }

    public ContextDTO getContextById(Long contextId) {
        Context context = contextService.findById(contextId);
        return contextService.getDTOFromEntity(context);
    }

    public Optional<Long> lookupContextByImpressionFormValues(String path, String name, String htmlId, Long organisationId, Long domainId, List<String> tags, List<String> labels) {
        Optional<Long> contextId = Optional.empty();
        List<Context> contexts = contextService.findByOrganisationId(organisationId);

        for (Context context : contexts) {
            if (context.getPath().equals(path) && context.getName().equals(name) && context.getHtmlId().equals(htmlId) && context.getDomainId().equals(domainId)) {
                List<ContextMeta> contextMetas = contextMetaService.findByContextId(context.getId());
                boolean tagsMatch = true;
                boolean labelsMatch = true;

                for (ContextMeta contextMeta : contextMetas) {
                    if (contextMeta.getType().equals(ContextMeta.Type.TAG) && !tags.contains(contextMeta.getValue())) {
                        tagsMatch = false;
                    }

                    if (contextMeta.getType().equals(ContextMeta.Type.LABEL) && !labels.contains(contextMeta.getValue())) {
                        labelsMatch = false;
                    }
                }

                if (tagsMatch && labelsMatch) {
                    contextId = Optional.of(context.getId());
                }
            }
        }

        return contextId;
    }

    private String generateContextCacheKey(ImpressionForm impressionForm, Long organisationId, Long domainId) {
        String tagStr = impressionForm.getTags() == null ? "" : impressionForm.getTags().toString();
        String labelStr = impressionForm.getLabels() == null ? "" : impressionForm.getLabels().toString();

        String content = String.join(":", impressionForm.getPath(), impressionForm.getName(), impressionForm.getHtmlId(), organisationId.toString(), domainId.toString(), tagStr, labelStr);
        return redisCacheLogic.getBase64dHash(content);
    }
}
