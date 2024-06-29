package org.wiliammelo.empoweru.mappers;

import org.hibernate.sql.Update;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.User;

@Mapper
public interface ProfessorMapper {

    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    Professor toProfessor(CreateProfessorDTO professor);

    @Mapping(source = "name", target = "user.name")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "gender", target = "user.gender")
    Professor toProfessor(ProfessorDTO professor);

    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.gender", target = "gender")
    ProfessorDTO toProfessorDTO(Professor professor);

    default User toUser(CreateProfessorDTO professor){
        User user = new User();
        user.setGender(professor.gender);
        user.setName(professor.name);
        user.setEmail(professor.email);
        user.setPassword(professor.password);
        return user;
    }

}
