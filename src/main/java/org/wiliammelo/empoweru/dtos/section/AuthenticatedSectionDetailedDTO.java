package org.wiliammelo.empoweru.dtos.section;

import lombok.Data;
import org.wiliammelo.empoweru.dtos.video.VideoWithStatusDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class AuthenticatedSectionDetailedDTO implements Serializable {

    private UUID id;

    private String title;

    private String description;

    private List<VideoWithStatusDTO> videos;
}
