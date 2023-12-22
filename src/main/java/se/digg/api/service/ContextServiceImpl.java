package se.digg.api.service;

import org.springframework.stereotype.Service;
import se.digg.api.dto.ContextDTO;
import se.digg.api.model.Context;
import se.digg.api.repository.ContextRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class ContextServiceImpl implements ContextService {

    private final ContextRepository contextRepository;

    public ContextServiceImpl(final ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }

    public Context save(Context context) {
        return contextRepository.save(context);
    }

    public List<Context> findAll() {
        Iterator<Context> contextIterator = contextRepository.findAll().iterator();
        List<Context> contextList = new ArrayList<>();

        while (contextIterator.hasNext()) {
            Context Context = contextIterator.next();
            contextList.add(Context);
        }

        return contextList;
    }

    public Context findById(Long id) {
        Optional<Context> optionalContext = contextRepository.findById(id);
        return optionalContext.orElseThrow(() -> new IllegalArgumentException("Context meta not found"));
    }

    public List<Context> findByOrganisationId(Long organisationId) {
        return contextRepository.findByOrganisationId(organisationId);
    }

    public List<Context> findByDomainId(Long domainId) {
        return contextRepository.findByDomainId(domainId);
    }

    public List<Context> findByPath(String path) {
        return contextRepository.findByPath(path);
    }

    public List<Context> findByName(String name) {
        return contextRepository.findByName(name);
    }

    public List<Context> findByHtmlId(String htmlId) {
        return contextRepository.findByHtmlId(htmlId);
    }

    public Context update(Context context) {
        return contextRepository.save(context);
    }

    public void delete(Long id) {
        contextRepository.deleteById(id);
    }

    public void deleteAll() {
        contextRepository.deleteAll();
    }

    public Context getEntityFromDTO(ContextDTO contextDTO) {
        return new Context(contextDTO.getOrganisationId(), contextDTO.getDomainId(), contextDTO.getPath(), contextDTO.getName(), contextDTO.getHtmlId());
    }

    public ContextDTO getDTOFromEntity(Context context) {
        return new ContextDTO(context.getId(),
                context.getOrganisationId(),
                context.getDomainId(),
                context.getPath(),
                context.getName(),
                context.getHtmlId(),
                context.getCreatedAt(),
                context.getUpdatedAt()
        );
    }
}
