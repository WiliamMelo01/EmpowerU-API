package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.mappers.CourseMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;
import org.wiliammelo.empoweru.repositories.VideoRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing courses.
 * <p>
 * This class provides services for creating, retrieving, updating, and deleting courses. It interacts with the {@link CourseRepository},
 * {@link ProfessorRepository}, and {@link VideoRepository} to perform operations on {@link Course} entities.</p>
 */
@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final VideoRepository videoRepository;

    /**
     * Creates a new course based on the provided {@link CreateCourseDTO} object.
     *
     * @param createCourseDTO The course creation details.
     * @return The created course as a {@link CourseDTO}.
     * @throws UserNotFoundException if the professor with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    @Transactional
    public CourseDTO create(CreateCourseDTO createCourseDTO) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(UUID.fromString(createCourseDTO.getProfessorId()))
                .orElseThrow(UserNotFoundException::new);

        Course course = CourseMapper.INSTANCE.toCourse(createCourseDTO);
        course.setProfessor(professor);
        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    /**
     * Retrieves all courses from the repository.
     *
     * @return A list of {@link CourseDTO} representing all courses in the repository.
     */
    @Cacheable("course")
    public List<CourseDTO> findAll() {
        List<Course> courseList = (List<Course>) this.courseRepository.findAll();
        return courseList.stream()
                .map(CourseMapper.INSTANCE::toCourseDto)
                .toList();
    }

    /**
     * Finds a course by its ID.
     *
     * @param id The UUID of the course to be found.
     * @return The found course as a {@link CourseDTO}.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @Cacheable(value = "course", key = "#id")
    public CourseDTO findById(UUID id) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
        return CourseMapper.INSTANCE.toCourseDto(course);
    }

    /**
     * Finds courses by title.
     *
     * @param title The title to search for in course titles.
     * @return A list of {@link CourseDTO} objects that match the title criteria.
     */
    @Cacheable(value = "course", key = "#title")
    public List<CourseDTO> findByTitle(String title) {
        List<Course> courseList = this.courseRepository.findByTitleContainingIgnoreCase(title);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    /**
     * Finds courses by tags.
     *
     * @param tags The list of tags to search for in course tags.
     * @return A list of {@link CourseDTO} objects that match the tags criteria.
     */
    @Cacheable(value = "course", key = "#tags")
    public List<CourseDTO> findByTags(List<String> tags) {
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        List<Course> courseList = this.courseRepository.findByTagsContainingIgnoreCase(tagsInLowerCase);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    /**
     * Finds courses by title and tags.
     *
     * @param title The title to search for in course titles.
     * @param tags  The list of tags to search for in course tags.
     * @return A list of {@link CourseDTO} objects that match the title and tags criteria.
     */
    @Cacheable(value = "course", key = "#title + '|' + T(java.util.Objects).hash(#tags.stream().sorted().join(','))")
    public List<CourseDTO> findByTitleAndTags(String title, List<String> tags) {
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        List<Course> courseList = this.courseRepository.findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(title, tagsInLowerCase);
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).toList();
    }

    /**
     * Updates a course based on the provided {@link UpdateCourseDTO} object.
     *
     * @param id              The UUID of the course to be updated.
     * @param updateCourseDTO The DTO containing the new course details.
     * @return The updated course details as a {@link CourseDTO}.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    public CourseDTO update(UUID id, UpdateCourseDTO updateCourseDTO) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        course.setDescription(updateCourseDTO.getDescription());
        course.setTitle(updateCourseDTO.getTitle());
        course.setTags(updateCourseDTO.getTags());

        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    /**
     * Performs a transactional operation to delete a course and all associated videos.
     *
     * @param id The UUID of the course to be deleted.
     * @return A confirmation message indicating the course has been successfully deleted.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    @Transactional
    public String delete(UUID id) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
        this.videoRepository.deleteAllByCourse(course);
        this.courseRepository.delete(course);
        return "Course with id: " + id + " deleted successfully.";
    }

}
