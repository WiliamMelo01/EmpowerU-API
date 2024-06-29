package org.wiliammelo.empoweru.dtos.student;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentDTO {

    public UUID id;

    public String name;

    public String email;

    public char gender;

}
