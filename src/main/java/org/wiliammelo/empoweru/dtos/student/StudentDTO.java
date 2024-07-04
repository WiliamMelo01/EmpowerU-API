package org.wiliammelo.empoweru.dtos.student;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class StudentDTO implements Serializable {

    private UUID id;

    private String name;

    private String email;

    private String gender;

}
