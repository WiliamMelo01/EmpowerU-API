package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.exceptions.ProfessorNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.ProfessorMapper;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing professors.
 * <p>
 * This class provides services for creating, retrieving, updating, and deleting professors. It interacts with the {@link ProfessorRepository}
 * to perform operations on {@link Professor} entities.</p>
 */
@Service
@AllArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UserService userService;

    /**
     * Finds a professor by their ID.
     *
     * @param id The UUID of the professor to find.
     * @return The found professor as a {@link ProfessorDTO}.
     * @throws ProfessorNotFoundException if the professor with the specified ID does not exist.
     */
    @Cacheable(value = "professor", key = "#id")
    public ProfessorDTO findById(UUID id) throws ProfessorNotFoundException {
        Professor professor = this.professorRepository.findById(id)
                .orElseThrow(ProfessorNotFoundException::new);
        return ProfessorMapper.INSTANCE.toProfessorDTO(professor);
    }

    /**
     * Retrieves all professors.
     *
     * @return A list of {@link ProfessorDTO} representing all professors in the repository.
     */
    @Cacheable(value = "professor", key = "#root.method.name")
    public List<ProfessorDTO> findAll() {
        List<Professor> professors = (List<Professor>) professorRepository.findAll();
        return professors.stream().map(ProfessorMapper.INSTANCE::toProfessorDTO).collect(Collectors.toList());
    }

    /**
     * Deletes a professor based on user object.
     *
     * @param userId The id of the user to be updated.
     * @return A success message indicating the deletion.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     */
    @CacheEvict(value = "professor", allEntries = true)
    @Transactional
    public String delete(UUID userId) throws UserNotFoundException {
        Professor professor = this.professorRepository.findByUserId(userId);

        this.userService.deleteById(userId);
        this.professorRepository.delete(professor);
        return "Professor with ID: " + professor.getId() + " deleted successfully.";
    }

    /**
     * Deletes a professor by their ID.
     *
     * @param professorId The UUID of the professor to delete.
     * @return A success message indicating the deletion.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     */
    @CacheEvict(value = "professor", allEntries = true)
    @Transactional
    public String deleteById(UUID professorId) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new);

        this.userService.deleteById(professor.getUser().getId());
        this.professorRepository.delete(professor);
        return "Professor with ID: " + professor.getId() + " deleted successfully.";
    }

    /**
     * Updates a professor with the given {@link UpdateProfessorDTO} object.
     *
     * @param userId       The id of the user to be updated.
     * @param professorDTO The updated professor details.
     * @return The updated professor as a {@link ProfessorDTO}.
     * @throws ProfessorNotFoundException if the professor with the specified ID does not exist.
     */
    @CacheEvict(value = "professor", allEntries = true)
    @Transactional
    public ProfessorDTO update(UUID userId, UpdateProfessorDTO professorDTO) throws ProfessorNotFoundException {
        Professor professor = this.professorRepository.findByUserId(userId);

        User user = professor.getUser();
        user.setName(professorDTO.getName());
        user.setEmail(professorDTO.getEmail());
        user.setGender(professorDTO.getGender());
        professor.setBio(professorDTO.getBio());
        professor.setImageUrl(professorDTO.getImageUrl());

        return ProfessorMapper.INSTANCE.toProfessorDTO(professorRepository.save(professor));
    }

}
