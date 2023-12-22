package se.digg.api.service;

import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.model.ApiKey;

import java.util.List;

public interface ApiKeyService {

    ApiKey save(ApiKey apiKey);

    List<ApiKey> findAll();

    ApiKey findById(Long id);

    ApiKey findByOrganisationId(Long organisationId);

    ApiKey findByUuid4(String uuid4);

    ApiKey update(ApiKey apiKey);

    void delete(Long id);

    void deleteAll();

    ApiKey getEntityFromDTO(ApiKeyDTO apiKeyDTO);

    ApiKeyDTO getDTOFromEntity(ApiKey apiKey);
}
