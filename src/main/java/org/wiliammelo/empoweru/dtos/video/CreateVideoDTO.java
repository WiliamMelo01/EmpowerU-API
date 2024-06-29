package org.wiliammelo.empoweru.dtos.video;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateVideoDTO {

    private String title;

    private UUID courseId;

    private int displayOrder;

    private double durationInSeconds;

}
