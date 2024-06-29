package org.wiliammelo.empoweru.dtos.video;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VideoDTO {

    private UUID id;

    private String url;

    private String title;

    private int displayOrder;

    private double durationInSeconds;

}
