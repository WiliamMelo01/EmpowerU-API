package org.wiliammelo.empoweru.repositories;

import org.springframework.data.repository.CrudRepository;
import org.wiliammelo.empoweru.models.VideoWatched;

import java.util.List;
import java.util.UUID;

public interface VideoWatchedRepository extends CrudRepository<VideoWatched, UUID> {

    List<VideoWatched> findAllByStudentId(UUID studentId);

}
