package org.wiliammelo.empoweru.dtos.section;

import lombok.Data;

import java.util.UUID;

@Data
public class SectionDTO {

    private UUID id;

    private String title;

    private String description;

    private UUID courseId;
}
