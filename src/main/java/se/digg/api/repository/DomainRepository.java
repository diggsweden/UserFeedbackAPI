// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.Domain;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends CrudRepository<Domain, Long> {

    Optional<Domain> findByName(String name);

    List<Domain> findByOrganisationId(Long organisationalId);
}
