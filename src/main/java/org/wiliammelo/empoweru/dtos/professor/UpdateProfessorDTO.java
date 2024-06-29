package org.wiliammelo.empoweru.dtos.professor;

import lombok.Getter;

@Getter
public class UpdateProfessorDTO {
    public String name;

    public String email;

    public char gender;

    private String bio;

    private String imageUrl;
}
