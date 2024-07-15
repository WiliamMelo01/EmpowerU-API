package org.wiliammelo.empoweru.dtos.section;

import lombok.Data;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PublicSectionDetailedDTO implements Serializable {

    private UUID id;

    private String title;

    private String description;

    private List<VideoDTO> videos;
}
