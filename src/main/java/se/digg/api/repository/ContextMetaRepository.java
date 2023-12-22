package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.ContextMeta;

import java.util.List;

@Repository
public interface ContextMetaRepository extends CrudRepository<ContextMeta, Long> {

    List<ContextMeta> findByContextId(Long contextId);

    List<ContextMeta> findByValue(String value);

    List<ContextMeta> findByType(ContextMeta.Type type);
}
