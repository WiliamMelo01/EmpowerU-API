package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.User;

/**
 * Mapper interface for converting between Student entities and their DTO representations.
 * Utilizes MapStruct for mapping fields between source and target objects.
 */
@Mapper
public interface StudentMapper {

    /**
     * Instance of the StudentMapper for use where dependency injection is not available.
     */
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    /**
     * Converts a CreateStudentDTO to a User entity.
     *
     * @param createStudentDTO The CreateStudentDTO containing the new student's information.
     * @return The User entity populated with data from the DTO.
     */
    default User toUser(CreateStudentDTO createStudentDTO) {
        User user = new User();
        user.setGender(createStudentDTO.getGender());
        user.setName(createStudentDTO.getName());
        user.setEmail(createStudentDTO.getEmail());
        user.setPassword(createStudentDTO.getPassword());
        return user;
    }

    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student The Student entity to convert.
     * @return The converted StudentDTO.
     */
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.gender", target = "gender")
    StudentDTO toStudentDTO(Student student);

}
