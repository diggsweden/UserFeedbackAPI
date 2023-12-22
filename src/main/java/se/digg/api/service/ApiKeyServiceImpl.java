package se.digg.api.service;

import org.springframework.stereotype.Service;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.model.ApiKey;
import se.digg.api.repository.ApiKeyRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(final ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public ApiKey save(ApiKey apiKey) throws IllegalArgumentException {
        return apiKeyRepository.save(apiKey);
    }

    public List<ApiKey> findAll() {
        Iterator<ApiKey> apiKeyIterator = apiKeyRepository.findAll().iterator();
        List<ApiKey> apiKeyList = new ArrayList<>();

        while (apiKeyIterator.hasNext()) {
            ApiKey apiKey = apiKeyIterator.next();
            apiKeyList.add(apiKey);
        }

        return apiKeyList;
    }

    public ApiKey findById(Long id) {
        Optional<ApiKey> optionalApiKey = apiKeyRepository.findById(id);
        return optionalApiKey.orElseThrow(() -> new IllegalArgumentException("Api key not found"));
    }

    public ApiKey findByOrganisationId(Long organisationId) {
        Optional<ApiKey> optionalApiKey = apiKeyRepository.findByOrganisationId(organisationId);
        return optionalApiKey.orElseThrow(() -> new IllegalArgumentException("Api key not found"));
    }

    public ApiKey findByUuid4(String uuid4) {
        Optional<ApiKey> optionalApiKey = apiKeyRepository.findByUuid4(uuid4);
        return optionalApiKey.orElseThrow(() -> new IllegalArgumentException("Api key not found"));
    }

    public ApiKey update(ApiKey apiKey) {
        return apiKeyRepository.save(apiKey);
    }

    public void delete(Long id) {
        apiKeyRepository.deleteById(id);
    }

    public void deleteAll() {
        apiKeyRepository.deleteAll();
    }

    public ApiKey getEntityFromDTO(ApiKeyDTO apiKeyDTO) {
        return new ApiKey(apiKeyDTO.getOrganisationId());
    }

    public ApiKeyDTO getDTOFromEntity(ApiKey apiKey) {
        return new ApiKeyDTO(apiKey.getId(),
                apiKey.getOrganisationId(),
                apiKey.getUuid4(),
                apiKey.getCreatedAt(),
                apiKey.getUpdatedAt()
        );
    }
}
