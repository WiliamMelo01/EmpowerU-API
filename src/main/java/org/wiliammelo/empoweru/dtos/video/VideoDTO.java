package org.wiliammelo.empoweru.dtos.video;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class VideoDTO implements Serializable {

    private UUID id;

    private String url;

    private String title;

    private int displayOrder;

    private double durationInSeconds;

}
