package org.wiliammelo.empoweru.dtos.student;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Data
public class UpdateStudentDTO{

    public String name;

    public String email;

    public char gender;

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
