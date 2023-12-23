// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.logic;

import org.springframework.stereotype.Component;
import se.digg.api.service.*;

import javax.transaction.Transactional;

@Component
public class AdminLogic {
    private final OrganisationService organisationService;
    private final ApiKeyService apiKeyService;
    private final DomainService domainService;
    private final ContextService contextService;
    private final ContextMetaService contextMetaService;
    private final ImpressionService impressionService;
    private final RatingService ratingService;

    public AdminLogic(final OrganisationService organisationService, final ApiKeyService apiKeyService, final DomainService domainService, final ContextService contextService, final ContextMetaService contextMetaService, final ImpressionService impressionService, final RatingService ratingService) {
        this.organisationService = organisationService;
        this.apiKeyService = apiKeyService;
        this.domainService = domainService;
        this.contextService = contextService;
        this.contextMetaService = contextMetaService;
        this.impressionService = impressionService;
        this.ratingService = ratingService;
    }

    @Transactional
    public void clearAllData() {
        organisationService.deleteAll();
        apiKeyService.deleteAll();
        domainService.deleteAll();
        contextService.deleteAll();
        contextMetaService.deleteAll();
        impressionService.deleteAll();
        ratingService.deleteAll();
    }
}
