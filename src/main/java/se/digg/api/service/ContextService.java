package se.digg.api.service;

import se.digg.api.dto.ContextDTO;
import se.digg.api.model.Context;

import java.util.List;

public interface ContextService {

    Context save(Context context);

    List<Context> findAll();

    Context findById(Long id);

    List<Context> findByOrganisationId(Long organisationId);

    List<Context> findByDomainId(Long domainId);

    List<Context> findByPath(String path);

    List<Context> findByName(String name);

    List<Context> findByHtmlId(String htmlId);

    Context update(Context context);

    void delete(Long id);

    void deleteAll();

    Context getEntityFromDTO(ContextDTO contextDTO);

    ContextDTO getDTOFromEntity(Context context);
}
