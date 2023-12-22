package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.Impression;

import java.util.List;

@Repository
public interface ImpressionRepository extends CrudRepository<Impression, Long> {

    List<Impression> findByContextId(Long contextId);

    List<Impression> findByFingerprint(String fingerprint);

}
