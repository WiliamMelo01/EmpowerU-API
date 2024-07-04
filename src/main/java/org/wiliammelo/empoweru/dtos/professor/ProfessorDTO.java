package org.wiliammelo.empoweru.dtos.professor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfessorDTO {

    private UUID id;

    private String name;

    private String email;

    private String gender;

    private String bio;

    private String imageURL;

}
