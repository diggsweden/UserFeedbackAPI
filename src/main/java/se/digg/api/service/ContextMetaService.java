// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.service;

import se.digg.api.dto.ContextMetaDTO;
import se.digg.api.model.ContextMeta;

import java.util.List;

public interface ContextMetaService {

    ContextMeta save(ContextMeta contextMeta);

    List<ContextMeta> findAll();

    ContextMeta findById(Long id);

    List<ContextMeta> findByContextId(Long organisationId);

    List<ContextMeta> findByValue(String value);

    List<ContextMeta> findByType(ContextMeta.Type value);

    ContextMeta update(ContextMeta contextMeta);

    void delete(Long id);

    void deleteAll();

    ContextMeta getEntityFromDTO(ContextMetaDTO contextMetaDTO);

    ContextMetaDTO getDTOFromEntity(ContextMeta contextMeta);
}
