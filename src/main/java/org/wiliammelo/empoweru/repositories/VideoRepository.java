package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.Video;

import java.util.UUID;

@Repository
public interface VideoRepository extends CrudRepository<Video, UUID> {
}
