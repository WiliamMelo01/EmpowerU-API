package org.wiliammelo.empoweru.dtos.course;

import lombok.Getter;
import lombok.Setter;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CourseDTO implements Serializable {

    private UUID id;

    private String title;

    private String description;

    private List<String> tags;

    private ProfessorDTO professor;

    private int videosCount;

}
