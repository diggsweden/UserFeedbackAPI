package se.digg.api.service;

import org.springframework.stereotype.Service;
import se.digg.api.dto.ContextMetaDTO;
import se.digg.api.model.ContextMeta;
import se.digg.api.repository.ContextMetaRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class ContextMetaServiceImpl implements ContextMetaService {

    private final ContextMetaRepository contextMetaRepository;

    public ContextMetaServiceImpl(final ContextMetaRepository contextMetaRepository) {
        this.contextMetaRepository = contextMetaRepository;
    }

    public ContextMeta save(ContextMeta contextMeta) {
        return contextMetaRepository.save(contextMeta);
    }

    public List<ContextMeta> findAll() {
        Iterator<ContextMeta> contextMetaIterator = contextMetaRepository.findAll().iterator();
        List<ContextMeta> contextMetaList = new ArrayList<>();

        while (contextMetaIterator.hasNext()) {
            ContextMeta ContextMeta = contextMetaIterator.next();
            contextMetaList.add(ContextMeta);
        }

        return contextMetaList;
    }

    public ContextMeta findById(Long id) {
        Optional<ContextMeta> optionalContextMeta = contextMetaRepository.findById(id);
        return optionalContextMeta.orElseThrow(() -> new IllegalArgumentException("Context meta not found"));
    }

    public List<ContextMeta> findByContextId(Long contextId) {
        return contextMetaRepository.findByContextId(contextId);
    }

    public List<ContextMeta> findByValue(String value) {
        return contextMetaRepository.findByValue(value);
    }

    public List<ContextMeta> findByType(ContextMeta.Type type) {
        return contextMetaRepository.findByType(type);
    }

    public ContextMeta update(ContextMeta contextMeta) {
        return contextMetaRepository.save(contextMeta);
    }

    public void delete(Long id) {
        contextMetaRepository.deleteById(id);
    }

    public void deleteAll() {
        contextMetaRepository.deleteAll();
    }

    public ContextMeta getEntityFromDTO(ContextMetaDTO contextMetaDTO) {
        return new ContextMeta(contextMetaDTO.getContextId(), contextMetaDTO.getValue(), ContextMeta.Type.valueOf(contextMetaDTO.getType()));
    }

    public ContextMetaDTO getDTOFromEntity(ContextMeta contextMeta) {
        return new ContextMetaDTO(contextMeta.getId(),
                contextMeta.getContextId(),
                contextMeta.getValue(),
                contextMeta.getType().toString(),
                contextMeta.getCreatedAt(),
                contextMeta.getUpdatedAt()
        );
    }
}
