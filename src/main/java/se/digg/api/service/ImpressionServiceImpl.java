// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.service;

import org.springframework.stereotype.Service;
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.model.Impression;
import se.digg.api.repository.ImpressionRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class ImpressionServiceImpl implements ImpressionService {

    private final ImpressionRepository impressionRepository;

    public ImpressionServiceImpl(final ImpressionRepository impressionRepository) {
        this.impressionRepository = impressionRepository;
    }

    public Impression save(Impression Impression) throws IllegalArgumentException {
        return impressionRepository.save(Impression);
    }

    public List<Impression> findAll() {
        Iterator<Impression> ImpressionIterator = impressionRepository.findAll().iterator();
        List<Impression> ImpressionList = new ArrayList<>();

        while (ImpressionIterator.hasNext()) {
            Impression Impression = ImpressionIterator.next();
            ImpressionList.add(Impression);
        }

        return ImpressionList;
    }

    public Impression findById(Long id) {
        Optional<Impression> optionalImpression = impressionRepository.findById(id);
        return optionalImpression.orElseThrow(() -> new IllegalArgumentException("Impression not found"));
    }

    public List<Impression> findByContextId(Long contextId) {
        return impressionRepository.findByContextId(contextId);
    }

    public List<Impression> findByFingerprint(String fingerprint) {
        return impressionRepository.findByFingerprint(fingerprint);
    }

    public Impression update(Impression Impression) {
        return impressionRepository.save(Impression);
    }

    public void delete(Long id) {
        impressionRepository.deleteById(id);
    }

    public void deleteAll() {
        impressionRepository.deleteAll();
    }

    public Impression getEntityFromDTO(ImpressionDTO impressionDTO) {
        return new Impression(impressionDTO.getContextId(), impressionDTO.getFingerprint());
    }

    public ImpressionDTO getDTOFromEntity(Impression Impression) {
        return new ImpressionDTO(Impression.getId(),
                Impression.getContextId(),
                Impression.getFingerprint(),
                null,
                null,
                Impression.getCreatedAt(),
                Impression.getUpdatedAt()
        );
    }
}
