package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Professor;

import java.util.UUID;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, UUID> {
}
