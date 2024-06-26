package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Course;

import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID> {
}
