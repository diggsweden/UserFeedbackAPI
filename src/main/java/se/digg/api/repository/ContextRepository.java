package se.digg.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.digg.api.model.Context;

import java.util.List;

@Repository
public interface ContextRepository extends CrudRepository<Context, Long> {

    List<Context> findByOrganisationId(Long organisationId);

    List<Context> findByDomainId(Long domainId);

    List<Context> findByPath(String path);

    List<Context> findByName(String name);

    List<Context> findByHtmlId(String htmlId);
}
