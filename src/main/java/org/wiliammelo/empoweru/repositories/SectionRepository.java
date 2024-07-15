package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Section;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionRepository extends CrudRepository<Section, UUID> {

    List<Section> findAllByCourse(Course course);

}
