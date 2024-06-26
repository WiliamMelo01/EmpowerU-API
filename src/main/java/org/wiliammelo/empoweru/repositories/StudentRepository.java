package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Student;

import java.util.UUID;

@Repository
public interface StudentRepository extends CrudRepository<Student, UUID> {
}
