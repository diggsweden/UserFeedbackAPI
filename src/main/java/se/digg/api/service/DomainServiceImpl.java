package se.digg.api.service;

import org.springframework.stereotype.Service;
import se.digg.api.dto.DomainDTO;
import se.digg.api.model.Domain;
import se.digg.api.repository.DomainRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;

    public DomainServiceImpl(final DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    public List<Domain> findAll() {
        Iterator<Domain> domainIterator = domainRepository.findAll().iterator();
        List<Domain> domainList = new ArrayList<>();

        while (domainIterator.hasNext()) {
            Domain domain = domainIterator.next();
            domainList.add(domain);
        }

        return domainList;
    }

    public Domain findById(Long id) {
        Optional<Domain> optionalDomain = domainRepository.findById(id);
        return optionalDomain.orElseThrow(() -> new IllegalArgumentException("Domain not found"));
    }

    public Domain findByName(String name) {
        Optional<Domain> optionalDomain = domainRepository.findByName(name);
        return optionalDomain.orElseThrow(() -> new IllegalArgumentException("Domain not found"));
    }

    public List<Domain> findByOrganisationId(Long organisationId) {
        return domainRepository.findByOrganisationId(organisationId);
    }

    public Domain update(Domain domain) {
        return domainRepository.save(domain);
    }

    public void delete(Long id) {
        domainRepository.deleteById(id);
    }

    public void deleteAll() {
        domainRepository.deleteAll();
    }

    public Domain getEntityFromDTO(DomainDTO domainDTO) {
        return new Domain(domainDTO.getName(), domainDTO.getOrganisationId());
    }

    public DomainDTO getDTOFromEntity(Domain domain) {
        return new DomainDTO(domain.getId(),
                domain.getName(),
                domain.getOrganisationId(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
