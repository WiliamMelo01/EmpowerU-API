package org.wiliammelo.empoweru.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.section.CreateSectionDTO;
import org.wiliammelo.empoweru.dtos.section.SectionDTO;
import org.wiliammelo.empoweru.dtos.section.UpdateSectionDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.ProfessorNotFoundException;
import org.wiliammelo.empoweru.exceptions.SectionNotFoundException;
import org.wiliammelo.empoweru.exceptions.UnauthorizedException;
import org.wiliammelo.empoweru.mappers.SectionMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Section;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;
import org.wiliammelo.empoweru.repositories.SectionRepository;
import org.wiliammelo.empoweru.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing section-related operations.
 * Provides functionality to create, find, delete, and update sections associated with courses.
 */
@Service
@AllArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;

    /**
     * Creates a new section based on the provided DTO and associates it with a course.
     *
     * @param createSectionDTO DTO containing section creation details.
     * @param requesterId      The UUID of the user requesting the creation.
     * @return The created SectionDTO.
     * @throws UnauthorizedException   If the requester is not authorized to create the section.
     * @throws CourseNotFoundException If the associated course is not found.
     */
    public SectionDTO create(CreateSectionDTO createSectionDTO, UUID requesterId) throws UnauthorizedException, CourseNotFoundException, ProfessorNotFoundException {
        Course course = courseRepository.findById(UUID.fromString(createSectionDTO.getCourseId()))
                .orElseThrow(CourseNotFoundException::new);

        if (!isTheOwner(requesterId, UUID.fromString(createSectionDTO.getCourseId()))) {
            throw new UnauthorizedException("You are not the owner of this course.");
        }

        Section section = SectionMapper.INSTANCE.toSection(createSectionDTO);
        section.setCourse(course);
        return SectionMapper.INSTANCE.toSectionDTO(sectionRepository.save(section));
    }

    /**
     * Finds a section by its ID.
     *
     * @param id The UUID of the section to find.
     * @return The found Section.
     * @throws SectionNotFoundException If the section is not found.
     */
    public Section findById(UUID id) throws SectionNotFoundException {
        return sectionRepository.findById(id)
                .orElseThrow(SectionNotFoundException::new);
    }

    /**
     * Finds all sections associated with a given course.
     *
     * @param courseId The UUID of the course.
     * @return A list of SectionDTOs for the sections found.
     * @throws CourseNotFoundException If the course is not found.
     */
    public List<SectionDTO> findAllByCourse(UUID courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        return sectionRepository.findAllByCourse(course).stream()
                .map(SectionMapper.INSTANCE::toSectionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a section by its ID.
     *
     * @param id          The UUID of the section to delete.
     * @param requesterId The UUID of the user requesting the deletion.
     * @return A confirmation message.
     * @throws SectionNotFoundException If the section is not found.
     * @throws UnauthorizedException    If the requester is not authorized to delete the section.
     */
    public String delete(UUID id, UUID requesterId) throws SectionNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        Section section = findById(id);

        if (isTheOwner(requesterId, section.getCourse().getId())) {
            return deleteProcess(section, id);
        }
        if (userRepository.isAdmin(requesterId)) {
            return deleteProcess(section, id);
        }

        throw new UnauthorizedException("You are not authorized to delete this section.");
    }

    /**
     * Deletes the specified section from the repository.
     * This method directly interacts with the {@link SectionRepository} to remove the given section.
     * After successful deletion, it returns a confirmation message.
     *
     * @param section The {@link Section} object to be deleted.
     * @param id      The UUID of the section being deleted, used for generating the confirmation message.
     * @return A string confirmation message indicating successful deletion.
     */
    private String deleteProcess(Section section, UUID id) {
        sectionRepository.delete(section);

        return "Section with id " + id + " deleted successfully!";
    }

    /**
     * Updates an existing section with new details provided in the {@link UpdateSectionDTO}.
     *
     * @param id               The UUID of the section to update.
     * @param updateSectionDTO The DTO containing the new title and description for the section.
     * @param requesterId      The UUID of the user requesting the update.
     * @return The updated {@link SectionDTO}.
     * @throws SectionNotFoundException If the section with the specified ID does not exist.
     * @throws UnauthorizedException    If the requester is not the owner of the course to which the section belongs.
     */
    public SectionDTO update(UUID id, UpdateSectionDTO updateSectionDTO, UUID requesterId) throws SectionNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        Section section = findById(id);

        if (!isTheOwner(requesterId, section.getCourse().getId())) {
            throw new UnauthorizedException("You are not the owner of this course.");
        }

        section.setTitle(updateSectionDTO.getTitle());
        section.setDescription(updateSectionDTO.getDescription());

        return SectionMapper.INSTANCE.toSectionDTO(sectionRepository.save(section));
    }

    /**
     * Checks if the specified requester is the owner of the course.
     *
     * @param requesterId The UUID of the requester.
     * @param courseId    The UUID of the course to check ownership for.
     * @return true if the requester is the owner of the course, false otherwise.
     */
    private boolean isTheOwner(UUID requesterId, UUID courseId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findById(requesterId)
                .orElseThrow(ProfessorNotFoundException::new);
        return courseRepository.isTheOwner(courseId, professor.getId());
    }

}
