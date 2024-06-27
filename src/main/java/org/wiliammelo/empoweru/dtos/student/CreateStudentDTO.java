package org.wiliammelo.empoweru.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentDTO {

    public String name;

    public String email;

    public String password;

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
