package se.digg.api.service;

import se.digg.api.dto.DomainDTO;
import se.digg.api.model.Domain;

import java.util.List;

public interface DomainService {

    Domain save(Domain domain);

    List<Domain> findAll();

    Domain findById(Long id);

    Domain findByName(String name);

    List<Domain> findByOrganisationId(Long organisationId);

    Domain update(Domain domain);

    void delete(Long id);

    void deleteAll();

    Domain getEntityFromDTO(DomainDTO domainDTO);

    DomainDTO getDTOFromEntity(Domain domain);
}
