package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CourseDetailedDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.*;
import org.wiliammelo.empoweru.mappers.CourseMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.repositories.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    /**
     * Creates a new course based on the provided {@link CreateCourseDTO} object.
     *
     * @param createCourseDTO The course creation details.
     * @param userId          The UUID of the user creating the course.
     * @return The created course as a {@link CourseDTO}.
     * @throws UserNotFoundException if the professor with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    @Transactional
    public CourseDTO create(CreateCourseDTO createCourseDTO, UUID userId) throws UserNotFoundException {
        Professor professor = this.professorRepository.findByUserId(userId);

        Course course = CourseMapper.INSTANCE.toCourse(createCourseDTO);
        course.setProfessor(professor);
        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    /**
     * Retrieves all courses from the repository.
     *
     * @return A list of {@link CourseDTO} representing all courses in the repository.
     */
    @Cacheable(value = "course", key = "#root.method.name")
    public List<CourseDTO> findAll() {
        List<Course> courseList = (List<Course>) this.courseRepository.findAll();

        return courseList.stream()
                .map(course -> {
                    CourseDTO dto = CourseMapper.INSTANCE.toCourseDto(course);
                    dto.setVideosCount(course.getSections().stream().mapToInt(s -> s.getVideos().size()).sum());
                    dto.setDurationInSeconds(course.getSections().stream().mapToLong(s -> s.getVideos().stream().mapToLong(v -> (long) v.getDurationInSeconds()).sum()).sum());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Finds a course by its ID.
     *
     * @param id The UUID of the course to be found.
     * @return The found course as a {@link CourseDTO}.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @Cacheable(value = "course", key = "#id.toString()+#includeDetails")
    public Object findById(UUID id, boolean includeDetails) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        if (includeDetails) {
            CourseDetailedDTO dto = CourseMapper.INSTANCE.toCourseDetailedDto(course);
            dto.setVideosCount(course.getSections().stream().mapToInt(s -> s.getVideos().size()).sum());
            dto.setDurationInSeconds(course.getSections().stream().mapToLong(s -> s.getVideos().stream().mapToLong(v -> (long) v.getDurationInSeconds()).sum()).sum());
            return dto;
        }

        CourseDTO dto = CourseMapper.INSTANCE.toCourseDto(course);
        dto.setVideosCount(course.getSections().stream().mapToInt(s -> s.getVideos().size()).sum());
        dto.setDurationInSeconds(course.getSections().stream().mapToLong(s -> s.getVideos().stream().mapToLong(v -> (long) v.getDurationInSeconds()).sum()).sum());
        return dto;
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
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).collect(Collectors.toList());
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
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).collect(Collectors.toList());
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
        return courseList.stream().map(CourseMapper.INSTANCE::toCourseDto).collect(Collectors.toList());
    }

    /**
     * Updates a course based on the provided {@link UpdateCourseDTO} object.
     *
     * @param courseId        The UUID of the course to be updated.
     * @param updateCourseDTO The DTO containing the new course details.
     * @param requesterId     The UUID of the user requesting the deletion.
     * @return The updated course details as a {@link CourseDTO}.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    public CourseDTO update(UUID courseId, UpdateCourseDTO updateCourseDTO, UUID requesterId) throws CourseNotFoundException, UnauthorizedException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Professor professor = professorRepository.findByUserId(requesterId);

        if (professor != null && isTheOwner(courseId, professor.getId())) {
            return updateProcess(course, updateCourseDTO);
        }

        if (isAdmin(requesterId)) {
            return updateProcess(course, updateCourseDTO);
        }

        throw new UnauthorizedException("You are not the owner of this course.");
    }

    /**
     * Updated the specified course and associated videos from the database.
     *
     * @param course          The course entity to update.
     * @param updateCourseDTO The DTO containing the new course details.
     * @return The updated course details as a {@link CourseDTO}.
     */
    private CourseDTO updateProcess(Course course, UpdateCourseDTO updateCourseDTO) {
        course.setDescription(updateCourseDTO.getDescription());
        course.setTitle(updateCourseDTO.getTitle());
        course.setTags(updateCourseDTO.getTags());

        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    /**
     * Performs a transactional operation to delete a course and all associated videos.
     *
     * @param courseId    The UUID of the course to be deleted.
     * @param requesterId The UUID of the user requesting the deletion.
     * @return A confirmation message indicating the course has been successfully deleted.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     * @throws UnauthorizedException   if the requester is not authorized to delete the course.
     */
    @CacheEvict(value = "course", allEntries = true)
    @Transactional
    public String delete(UUID courseId, UUID requesterId) throws CourseNotFoundException, UnauthorizedException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Professor professor = professorRepository.findByUserId(requesterId);

        if (professor != null && isTheOwner(courseId, professor.getId())) {
            return deleteProcess(course);
        }

        if (isAdmin(requesterId)) {
            return deleteProcess(course);
        }

        throw new UnauthorizedException("You are not the owner of this course.");
    }

    /**
     * Deletes the specified course and associated videos from the database.
     *
     * @param course The course entity to delete.
     * @return A confirmation message indicating the course has been successfully deleted.
     */
    private String deleteProcess(Course course) {
        this.videoRepository.deleteAllByCourse(course);
        this.courseRepository.delete(course);
        return "Course with id: " + course.getId() + " deleted successfully.";
    }

    /**
     * Enrolls a student into a specified course.
     *
     * @param courseId The UUID of the course to enroll the student in.
     * @param userId   The UUID of the student to enroll in the course.
     * @return A confirmation message indicating the student has been successfully enrolled.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     * @throws UserNotFoundException   if the student with the specified ID does not exist.
     */
    public String enroll(UUID courseId, UUID userId) throws CourseNotFoundException, UserNotFoundException, UserAlreadyEnrolledException {

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        Student student = this.studentRepository.findByUserId(userId);

        if (student == null) {
            throw new UserNotFoundException();
        }

        if (course.getStudents().contains(student)) {
            throw new UserAlreadyEnrolledException();
        }

        course.getStudents().add(student);

        courseRepository.save(course);
        return "Student with id: " + student.getId() + " enrolled successfully.";
    }

    /**
     * Disenroll a student from a specific course.
     *
     * @param courseId The UUID of the course to disenroll the student from.
     * @param userId   The UUID of the student to disenroll from the course.
     * @return A confirmation message indicating the student has been successfully disenrolled.
     * @throws CourseNotFoundException  if the course with the specified ID does not exist.
     * @throws UserNotFoundException    if the student with the specified ID does not exist.
     * @throws UserNotEnrolledException if the student is not enrolled in the course.
     */
    public String disenroll(UUID courseId, UUID userId) throws CourseNotFoundException, UserNotFoundException, UserNotEnrolledException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        Student student = this.studentRepository.findByUserId(userId);

        if (student == null) {
            throw new UserNotFoundException();
        }

        boolean removed = course.getStudents().remove(student);

        if (removed) {
            courseRepository.save(course);
            return "Student with id: " + student.getId() + " disenrolled successfully.";
        }

        throw new UserNotEnrolledException();
    }

    /**
     * Checks if the specified user is the owner of the course.
     *
     * @param courseId    The UUID of the course to check ownership for.
     * @param requesterId The UUID of the user to verify as the owner.
     * @return true if the requesterId matches the owner of the course, false otherwise.
     */
    private boolean isTheOwner(UUID courseId, UUID requesterId) {
        return this.courseRepository.isTheOwner(courseId, requesterId);
    }

    /**
     * Determines if the specified user has administrative privileges.
     *
     * @param userId The UUID of the user to check for administrative privileges.
     * @return true if the user has administrative privileges, false otherwise.
     */
    private boolean isAdmin(UUID userId) {
        return this.userRepository.isAdmin(userId);
    }

}
