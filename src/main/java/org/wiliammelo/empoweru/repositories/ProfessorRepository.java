package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Professor;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, UUID> {
    Optional<Professor> findById(@Param("userId") UUID id);

}
