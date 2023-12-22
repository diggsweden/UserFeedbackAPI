package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.Organisation;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

    Optional<Organisation> findByName(String name);
}
