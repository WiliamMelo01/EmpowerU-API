package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.course.*;
import org.wiliammelo.empoweru.dtos.video.VideoWithStatusDTO;
import org.wiliammelo.empoweru.exceptions.*;
import org.wiliammelo.empoweru.mappers.CourseMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.models.VideoWatched;
import org.wiliammelo.empoweru.repositories.*;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing courses.
 * <p>
 * This class provides services for creating, retrieving, updating, and deleting courses. It interacts with the {@link CourseRepository},
 * {@link ProfessorRepository}, and {@link VideoRepository} to perform operations on {@link Course} entities.</p>
 */
@Service
@Log4j2
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final VideoWatchedRepository videoWatchedRepository;
    private final EnrollmentRepository enrollmentRepository;

    private static final int PAGE_SIZE = 10;

    /**
     * Creates a new course based on the provided {@link CreateCourseDTO} object.
     *
     * @param createCourseDTO The course creation details.
     * @param studentId       The UUID of the professor creating the course.
     * @return The created course as a {@link CourseDTO}.
     * @throws UserNotFoundException if the professor with the specified ID does not exist.
     */
    @CacheEvict(value = "course", allEntries = true)
    @Transactional
    public CourseDTO create(CreateCourseDTO createCourseDTO, UUID studentId) throws UserNotFoundException {
        Professor professor = this.professorRepository.findById(studentId)
                .orElseThrow(ProfessorNotFoundException::new);

        Course course = CourseMapper.INSTANCE.toCourse(createCourseDTO);
        course.setProfessor(professor);
        return CourseMapper.INSTANCE.toCourseDto(this.courseRepository.save(course));
    }

    /**
     * Retrieves all courses from the repository.
     *
     * @return A list of {@link CourseDTO} representing all courses in the repository.
     */
    @Cacheable(value = "course", key = "#root.method.name + '_' + #page + '_' + #pageSize")
    public List<CourseDTO> findAll(int page, int pageSize) {
        Page<Course> courseList = this.courseRepository.findAll(PageRequest.of(page, pageSize));

        return courseList
                .stream()
                .map(course -> {
                    CourseDTO dto = CourseMapper.INSTANCE.toCourseDto(course);
                    dto.setVideosCount(course.getSections().stream().mapToInt(s -> s.getVideos().size()).sum());
                    dto.setDurationInSeconds(this.getTotalDurationInSeconds(course));
                    return dto;
                }).toList();
    }

    /**
     * Finds a course by its ID.
     *
     * @param id The UUID of the course to be found.
     * @return The found course as a {@link CourseDTO}.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     */
    @Cacheable(value = "course", key = "#id.toString()+#includeDetails")
    public Object findByIdPublic(UUID id, boolean includeDetails) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        if (includeDetails) {
            PublicCourseDetailedDTO dto = CourseMapper.INSTANCE.toPublicCourseDetailedDto(course);
            dto.setVideosCount(this.getTotalVideosCount(course));
            dto.setDurationInSeconds(this.getTotalDurationInSeconds(course));
            return dto;
        }

        CourseDTO dto = CourseMapper.INSTANCE.toCourseDto(course);
        dto.setVideosCount(this.getTotalVideosCount(course));
        dto.setDurationInSeconds(this.getTotalDurationInSeconds(course));
        return dto;
    }

    /**
     * Retrieves detailed information about a course for an authenticated user.
     *
     * @param id        The UUID of the course to find.
     * @param studentId The UUID of the authenticated student.
     * @return A {@link AuthenticatedCourseDetailedDTO} object containing detailed information about the course, including watched videos and enrollment status.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     * @throws UserNotFoundException   if the student with the specified ID does not exist.
     */
    @Cacheable(value = "course", key = "#id.toString()+#studentId.toString()")
    public Object findByIdAuthenticated(UUID id, UUID studentId) throws CourseNotFoundException, UserNotFoundException {
        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

        Course course = this.courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        List<VideoWatched> videoWatchedList = videoWatchedRepository.findAllByStudentId(student.getId());

        AuthenticatedCourseDetailedDTO dto = CourseMapper.INSTANCE.toAuthenticatedCourseDetailedDTO(course);
        dto.setVideosCount(this.getTotalVideosCount(course));
        dto.setDurationInSeconds(this.getTotalDurationInSeconds(course));
        dto.getSections().forEach(section -> section.getVideos().forEach(video -> markVideoWatchedStatus(video, videoWatchedList)));
        dto.setEnrolled(enrollmentRepository.isStudentEnrolled(id, student.getId()));

        return dto;
    }

    /**
     * Finds courses by title.
     *
     * @param title The title to search for in course titles.
     * @return A list of {@link CourseDTO} objects that match the title criteria.
     */
    @Cacheable(value = "course", key = "#root.methodName+#args")
    public List<CourseDTO> findByTitle(String title, int page, int pageSize) {
        Page<Course> courseList = this.courseRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, pageSize));
        return courseList.stream()
                .map(CourseMapper.INSTANCE::toCourseDto)
                .toList();
    }

    /**
     * Finds courses by tags.
     *
     * @param tags The list of tags to search for in course tags.
     * @return A list of {@link CourseDTO} objects that match the tags criteria.
     */
    @Cacheable(value = "course", key = "#root.methodName+#args")
    public List<CourseDTO> findByTags(List<String> tags, int page, int pageSize) {
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        Page<Course> courseList = this.courseRepository.findByTagsContainingIgnoreCase(tagsInLowerCase, PageRequest.of(page, pageSize));
        return courseList.stream()
                .map(CourseMapper.INSTANCE::toCourseDto)
                .toList();
    }

    /**
     * Finds courses by title and tags.
     *
     * @param title The title to search for in course titles.
     * @param tags  The list of tags to search for in course tags.
     * @return A list of {@link CourseDTO} objects that match the title and tags criteria.
     */
    @Cacheable(value = "course", key = "#title + '|' + T(java.util.Objects).hash(#tags.stream().sorted().join(','))+#page+#pageSize")
    public List<CourseDTO> findByTitleAndTags(String title, List<String> tags, int page, int pageSize) {
        List<String> tagsInLowerCase = tags.stream().map(String::toLowerCase).toList();
        Page<Course> courseList = this.courseRepository.findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(title, tagsInLowerCase, PageRequest.of(page, pageSize));
        return courseList.stream()
                .map(CourseMapper.INSTANCE::toCourseDto)
                .toList();
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
    public CourseDTO update(UUID courseId, UpdateCourseDTO updateCourseDTO, UUID requesterId) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Professor professor = professorRepository.findById(requesterId)
                .orElseThrow(ProfessorNotFoundException::new);

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
    public String delete(UUID courseId, UUID requesterId) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Professor professor = professorRepository.findById(requesterId)
                .orElseThrow(ProfessorNotFoundException::new);

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
     * @param courseId  The UUID of the course to enroll the student in.
     * @param studentId The UUID of the student to enroll in the course.
     * @return A confirmation message indicating the student has been successfully enrolled.
     * @throws CourseNotFoundException if the course with the specified ID does not exist.
     * @throws UserNotFoundException   if the student with the specified ID does not exist.
     */
    @CacheEvict(value = "course", key = "#courseId.toString()+#studentId.toString()")
    public String enroll(UUID courseId, UUID studentId) throws CourseNotFoundException, UserNotFoundException, UserAlreadyEnrolledException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

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
     * @param courseId  The UUID of the course to disenroll the student from.
     * @param studentId The UUID of the student to disenroll from the course.
     * @return A confirmation message indicating the student has been successfully disenrolled.
     * @throws CourseNotFoundException  if the course with the specified ID does not exist.
     * @throws UserNotFoundException    if the student with the specified ID does not exist.
     * @throws UserNotEnrolledException if the student is not enrolled in the course.
     */
    @CacheEvict(value = "course", key = "#courseId.toString()+#studentId.toString()")
    public String disenroll(UUID courseId, UUID studentId) throws CourseNotFoundException, UserNotFoundException, UserNotEnrolledException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

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

    /**
     * Returns the total number of videos in a course.
     *
     * @param course The course to count videos from.
     * @return The total number of videos in the course.
     */
    private int getTotalVideosCount(Course course) {
        return course.getSections().stream()
                .mapToInt(section -> section.getVideos().size())
                .sum();
    }

    /**
     * Returns the total duration of all videos in the course in seconds.
     *
     * @param course The course to calculate duration from.
     * @return The total duration in seconds.
     */
    private long getTotalDurationInSeconds(Course course) {
        return course.getSections().stream()
                .mapToLong(section -> section.getVideos().stream()
                        .mapToLong(video -> (long) video.getDurationInSeconds())
                        .sum())
                .sum();
    }

    /**
     * Marks a video watched status based on the provided list of watched videos.
     *
     * @param video         The video to mark.
     * @param videosWatched The list of watched videos.
     */
    private void markVideoWatchedStatus(VideoWithStatusDTO video, List<VideoWatched> videosWatched) {
        boolean isWatched = videosWatched.stream()
                .anyMatch(vw -> vw.getVideoId().equals(video.getId()));
        video.setWatched(isWatched);
    }

}
