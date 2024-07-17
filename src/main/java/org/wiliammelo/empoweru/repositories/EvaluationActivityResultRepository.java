package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.EvaluationActivityResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvaluationActivityResultRepository extends CrudRepository<EvaluationActivityResult, UUID> {

    Optional<EvaluationActivityResult> findByEvaluationIdAndStudentId(UUID evaluationId, UUID studentId);

    List<EvaluationActivityResult> findAllByCourseIdAndStudentId(UUID courseId, UUID studentId);

}
