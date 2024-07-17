package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.QuestionOption;

import java.util.UUID;

@Repository
public interface QuestionOptionRepository extends CrudRepository<QuestionOption, UUID> {
    
}

