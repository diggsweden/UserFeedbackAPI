// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.service;

import se.digg.api.dto.OrganisationDTO;
import se.digg.api.model.Organisation;

import java.util.List;
import java.util.Optional;

public interface OrganisationService {

    Organisation save(Organisation organisation);

    List<Organisation> findAll();

    Organisation findById(Long id);

    Optional<Organisation> findByName(String name);

    Organisation update(Organisation organisation);

    void delete(Long id);

    void deleteAll();

    Organisation getEntityFromDTO(OrganisationDTO organisationDTO);

    OrganisationDTO getDTOFromEntity(Organisation organisation);
}
