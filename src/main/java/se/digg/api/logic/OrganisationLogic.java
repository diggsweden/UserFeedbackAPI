package se.digg.api.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.dto.DomainDTO;
import se.digg.api.dto.OrganisationDTO;
import se.digg.api.form.RegisterForm;
import se.digg.api.model.ApiKey;
import se.digg.api.model.Domain;
import se.digg.api.model.Organisation;
import se.digg.api.service.ApiKeyService;
import se.digg.api.service.DomainService;
import se.digg.api.service.OrganisationService;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class OrganisationLogic {

    private ApiKeyService apiKeyService;
    private DomainService domainService;
    private OrganisationService organisationService;

    @Autowired
    private void setApiKeyService(final ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Autowired
    private void setDomainService(final DomainService domainService) {
        this.domainService = domainService;
    }

    @Autowired
    private void setOrganisationService(final OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @Transactional
    public ApiKeyDTO registerOrganisationAndGetAccessToken(RegisterForm registerForm) {
        OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.setName(registerForm.getOrganisationName());

        Organisation newOrganisation = organisationService.save(organisationService.getEntityFromDTO(organisationDTO));

        List<String> domainList = registerForm.getDomainList();
        for (String domain : domainList) {

            // TODO: Future development - Check if domain name is valid (pattern: domainName.tld)

            DomainDTO domainDTO = new DomainDTO();
            domainDTO.setName(domain);
            domainDTO.setOrganisationId(newOrganisation.getId());
            domainService.save(domainService.getEntityFromDTO(domainDTO));
        }

        ApiKeyDTO apiKeyDTO = new ApiKeyDTO();
        apiKeyDTO.setOrganisationId(newOrganisation.getId());

        ApiKey apiKey = apiKeyService.save(apiKeyService.getEntityFromDTO(apiKeyDTO));

        return apiKeyService.getDTOFromEntity(apiKey);
    }

    public Long getOrganisationIdByApiKeyUuid4(String uuid4) {
        ApiKey apiKey = apiKeyService.findByUuid4(uuid4);
        return apiKey.getOrganisationId();
    }

    public DomainDTO getDomainByDomainName(String name) {
        Domain domain = domainService.findByName(name);
        return domainService.getDTOFromEntity(domain);
    }
}
