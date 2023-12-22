package se.digg.api.service;


import org.springframework.stereotype.Service;
import se.digg.api.dto.OrganisationDTO;
import se.digg.api.model.Organisation;
import se.digg.api.repository.OrganisationRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationServiceImpl(final OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public Organisation save(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public List<Organisation> findAll() {
        Iterator<Organisation> organisationIterator = organisationRepository.findAll().iterator();
        List<Organisation> organisationList = new ArrayList<>();

        while (organisationIterator.hasNext()) {
            Organisation organisation = organisationIterator.next();
            organisationList.add(organisation);
        }

        return organisationList;
    }

    public Organisation findById(Long id) {
        Optional<Organisation> optionalOrganisation = organisationRepository.findById(id);
        return optionalOrganisation.orElseThrow(() -> new IllegalArgumentException("Organisation not found"));
    }

    public Optional<Organisation> findByName(String name) {
        return organisationRepository.findByName(name);
    }

    public Organisation update(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public void delete(Long id) {
        organisationRepository.deleteById(id);
    }

    public void deleteAll() {
        organisationRepository.deleteAll();
    }

    public Organisation getEntityFromDTO(OrganisationDTO organisationDTO) {
        return new Organisation(organisationDTO.getName());
    }

    public OrganisationDTO getDTOFromEntity(Organisation organisation) {
        return new OrganisationDTO(organisation.getId(),
                organisation.getName(),
                organisation.getCreatedAt(),
                organisation.getUpdatedAt()
        );
    }
}
