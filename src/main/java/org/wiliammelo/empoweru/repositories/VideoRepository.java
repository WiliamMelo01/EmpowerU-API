package org.wiliammelo.empoweru.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Video;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends CrudRepository<Video, UUID> {

    @Query("SELECT v FROM Video v WHERE v.displayOrder >= :display_order")
    List<Video> findAllByWithDisplayOrderBiggerThanOrEqual(@Param("display_order") int displayOrder);

    @Modifying
    @Query("DELETE FROM Video v WHERE v.section.course = :course")
    void deleteAllByCourse(@Param("course") Course course);
}
