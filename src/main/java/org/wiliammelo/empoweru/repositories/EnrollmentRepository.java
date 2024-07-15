package org.wiliammelo.empoweru.repositories;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class EnrollmentRepository {

    private final EntityManager entityManager;

    public boolean isStudentEnrolled(UUID courseId, UUID studentId) {
        return entityManager.createQuery(
                        "SELECT COUNT(s) > 0 FROM Course c JOIN c.students s WHERE c.id = :courseId AND s.id = :studentId", Boolean.class)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .getSingleResult();
    }

}
