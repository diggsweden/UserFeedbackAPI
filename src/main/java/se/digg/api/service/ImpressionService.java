package se.digg.api.service;

import se.digg.api.dto.ImpressionDTO;
import se.digg.api.model.Impression;

import java.util.List;

public interface ImpressionService {

    Impression save(Impression impression);

    List<Impression> findAll();

    Impression findById(Long id);

    List<Impression> findByContextId(Long contextId);

    List<Impression> findByFingerprint(String fingerprint);

    Impression update(Impression impression);

    void delete(Long id);

    void deleteAll();

    Impression getEntityFromDTO(ImpressionDTO impressionDTO);

    ImpressionDTO getDTOFromEntity(Impression impression);
}
