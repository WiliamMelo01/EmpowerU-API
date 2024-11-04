package org.wiliammelo.empoweru.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Course;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Page<Course> findAll(Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE LOWER(t) IN :tags")
    Page<Course> findByTagsContainingIgnoreCase(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Course c JOIN c.tags t WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) AND LOWER(t) IN :tags")
    Page<Course> findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(@Param("title") String title, @Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT (CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END) FROM Course c WHERE c.id = :courseId AND c.professor.id = :ownerId")
    Boolean isTheOwner(@Param("courseId") UUID courseId, @Param("ownerId") UUID ownerId);

    boolean existsById(UUID id);

}
