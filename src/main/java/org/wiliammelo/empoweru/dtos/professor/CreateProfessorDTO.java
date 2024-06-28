package org.wiliammelo.empoweru.dtos.professor;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Data
public class CreateProfessorDTO {

    public String name;

    public String email;

    public String password;

    public char gender;

    private String bio;

    private String imageUrl;

    public User toUser(){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }


    public Professor toProfessor(){
        Professor professor = new Professor();
        BeanUtils.copyProperties(this, professor);
        return professor;
    }

}
