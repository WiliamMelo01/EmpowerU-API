package org.wiliammelo.empoweru.dtos.student;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentDTO {

    private UUID id;

    private String name;

    private String email;

    private String gender;

}
