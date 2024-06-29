package org.wiliammelo.empoweru.dtos.professor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfessorDTO {

    private UUID id;

    public String name;

    public String email;

    public char gender;

    private String bio;

    private String imageURL;

}
