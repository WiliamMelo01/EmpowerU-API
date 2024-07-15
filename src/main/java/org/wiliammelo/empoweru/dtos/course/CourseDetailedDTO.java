package org.wiliammelo.empoweru.dtos.course;

import lombok.Data;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.section.SectionDetailedDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class CourseDetailedDTO implements Serializable {

    private UUID id;

    private String title;

    private String description;

    private List<String> tags;

    private ProfessorDTO professor;

    private int videosCount;

    private long durationInSeconds;

    private List<SectionDetailedDTO> sections;

}
