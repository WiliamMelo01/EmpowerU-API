package org.wiliammelo.empoweru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Student;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends CrudRepository<Student, UUID> {

    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s WHERE s.user.id = :userId")
    Student findByUserId(@Param("userId") UUID userId);

}
