package org.wiliammelo.empoweru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.User;

/**
 * Mapper interface for converting between Professor entities and their DTO representations.
 * Utilizes MapStruct for mapping fields between source and target objects.
 */
@Mapper
public interface ProfessorMapper {

    /**
     * Instance of the ProfessorMapper for use where dependency injection is not available.
     */
    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    /**
     * Converts a Professor entity to a ProfessorDTO, mapping fields from the nested User entity.
     *
     * @param professor The Professor entity to convert.
     * @return The converted ProfessorDTO.
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "imageUrl", target = "imageURL")
    ProfessorDTO toProfessorDTO(Professor professor);

    /**
     * Helper method to convert a CreateProfessorDTO to a User entity.
     * This method is used internally for setting up the User entity within a Professor.
     *
     * @param professor The CreateProfessorDTO containing user information.
     * @return The User entity populated with data from the DTO.
     */
    default User toUser(CreateProfessorDTO professor) {
        User user = new User();
        user.setGender(professor.gender);
        user.setName(professor.name);
        user.setEmail(professor.email);
        user.setPassword(professor.password);
        return user;
    }

    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Professor toProfessor(CreateProfessorDTO createProfessorDTO);
}
