package org.wiliammelo.empoweru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Professor;

import java.util.UUID;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, UUID> {
    @Query("SELECT p FROM Professor p WHERE p.user.id = :userId")
    Professor findByUserId(@Param("userId") UUID id);

}
