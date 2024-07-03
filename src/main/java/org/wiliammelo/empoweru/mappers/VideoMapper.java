package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.video.CreateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;
import org.wiliammelo.empoweru.models.Video;

/**
 * Mapper interface for converting between Video entities and their DTO representations.
 * Utilizes MapStruct for mapping fields between source and target objects.
 */
@Mapper
public interface VideoMapper {

    /**
     * Instance of the VideoMapper for use where dependency injection is not available.
     */
    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    /**
     * Converts a Video entity to a VideoDTO.
     *
     * @param video The Video entity to convert.
     * @return The converted VideoDTO.
     */
    VideoDTO toVideoDTO(Video video);

    /**
     * Converts a CreateVideoDTO to a Video entity.
     *
     * @param videoDTO The CreateVideoDTO to convert.
     * @return The converted Video entity.
     */
    Video toVideo(CreateVideoDTO videoDTO);

}
