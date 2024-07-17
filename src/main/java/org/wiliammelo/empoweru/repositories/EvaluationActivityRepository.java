package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.EvaluationActivity;

import java.util.UUID;

@Repository
public interface EvaluationActivityRepository extends CrudRepository<EvaluationActivity, UUID> {

    boolean existsById(UUID id);

}
