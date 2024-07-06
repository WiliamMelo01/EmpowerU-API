package org.wiliammelo.empoweru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Course;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID> {

    List<Course> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE LOWER(t) IN :tags")
    List<Course> findByTagsContainingIgnoreCase(@Param("tags") List<String> tags);

    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) AND LOWER(t) IN :tags")
    List<Course> findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(@Param("title") String title, @Param("tags") List<String> tags);

    @Query("SELECT CASE WHEN c.professor.id = :ownerId THEN TRUE ELSE FALSE END FROM Course c WHERE c.id = :courseId")
    boolean isTheOwner(@Param("courseId") UUID courseId, @Param("ownerId") UUID ownerId);
}
