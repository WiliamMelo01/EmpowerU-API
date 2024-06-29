package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    default User toUser(CreateStudentDTO createStudentDTO){
        User user = new User();
        user.setGender(createStudentDTO.getGender());
        user.setName(createStudentDTO.getName());
        user.setEmail(createStudentDTO.getEmail());
        user.setPassword(createStudentDTO.getPassword());
        return user;
    }

    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.gender", target = "gender")
    StudentDTO toStudentDTO(Student student);

}
