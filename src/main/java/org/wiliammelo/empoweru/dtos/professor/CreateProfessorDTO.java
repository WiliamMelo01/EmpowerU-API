package org.wiliammelo.empoweru.dtos.professor;

import lombok.Data;

@Data
public class CreateProfessorDTO {

    public String name;

    public String email;

    public String password;

    public char gender;

    private String bio;

    private String imageUrl;

}
