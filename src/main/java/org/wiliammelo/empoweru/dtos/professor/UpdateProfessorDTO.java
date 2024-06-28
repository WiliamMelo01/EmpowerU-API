package org.wiliammelo.empoweru.dtos.professor;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Getter
public class UpdateProfessorDTO {
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


    public Student toStudent(){
        Student student = new Student();
        BeanUtils.copyProperties(this, student);
        return student;
    }
}
