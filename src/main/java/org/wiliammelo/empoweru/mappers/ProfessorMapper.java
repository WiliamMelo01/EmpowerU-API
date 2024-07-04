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
     * Converts a CreateProfessorDTO to a Professor entity.
     *
     * @param professor The CreateProfessorDTO to convert.
     * @return The converted Professor entity.
     */
    Professor toProfessor(CreateProfessorDTO professor);

    /**
     * Converts a ProfessorDTO to a Professor entity, mapping DTO fields to the nested User entity.
     *
     * @param professor The ProfessorDTO to convert.
     * @return The converted Professor entity with nested User entity populated.
     */
    @Mapping(source = "name", target = "user.name")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "gender", target = "user.gender")
    Professor toProfessor(ProfessorDTO professor);

    /**
     * Converts a Professor entity to a ProfessorDTO, mapping fields from the nested User entity.
     *
     * @param professor The Professor entity to convert.
     * @return The converted ProfessorDTO.
     */
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.gender", target = "gender")
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

}
