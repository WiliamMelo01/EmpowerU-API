package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Question;

import java.util.UUID;

@Repository
public interface QuestionRepository extends CrudRepository<Question, UUID> {
}
