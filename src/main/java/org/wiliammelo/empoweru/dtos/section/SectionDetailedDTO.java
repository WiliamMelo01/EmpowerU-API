package org.wiliammelo.empoweru.dtos.section;

import lombok.Data;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;

import java.util.List;
import java.util.UUID;

@Data
public class SectionDetailedDTO {

    private UUID id;

    private String title;

    private String description;

    List<VideoDTO> videos;

}
