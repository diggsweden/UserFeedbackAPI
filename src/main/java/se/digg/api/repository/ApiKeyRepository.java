package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.ApiKey;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKey, Long> {

    Optional<ApiKey> findByOrganisationId(Long organisationId);

    Optional<ApiKey> findByUuid4(String uuid4);
}
