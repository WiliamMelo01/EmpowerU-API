package org.wiliammelo.empoweru.dtos.professor;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Getter
public class UpdateProfessorDTO {
    public String name;

    public String email;

    public char gender;

    private String bio;

    private String imageUrl;
}
