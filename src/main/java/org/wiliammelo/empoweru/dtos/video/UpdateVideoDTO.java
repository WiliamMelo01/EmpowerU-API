package org.wiliammelo.empoweru.dtos.video;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateVideoDTO {

    private String title;

    private UUID courseId;

    private double durationInSeconds;

}
