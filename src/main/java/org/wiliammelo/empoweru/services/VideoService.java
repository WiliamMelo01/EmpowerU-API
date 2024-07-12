package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wiliammelo.empoweru.dtos.video.CreateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.UpdateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.InvalidFileTypeException;
import org.wiliammelo.empoweru.exceptions.UnauthorizedException;
import org.wiliammelo.empoweru.exceptions.VideoNotFoundException;
import org.wiliammelo.empoweru.file_upload.FileUploader;
import org.wiliammelo.empoweru.mappers.VideoMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.models.Video;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.ProfessorRepository;
import org.wiliammelo.empoweru.repositories.UserRepository;
import org.wiliammelo.empoweru.repositories.VideoRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing video-related operations.
 * Provides functionality to create, find, delete, and update videos associated with courses.
 * Utilizes {@link VideoRepository} for persistence operations.
 * Utilizes {@link CourseRepository} for persistence operations.
 * Utilizes {@link FileUploader} for handling file uploads.
 */
@Service
@AllArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final FileUploader fileUploader;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;

    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("video/mp4", "video/mkv");
    private static final String UNAUTHORIZED_MESSAGE = "You're not the owner of this course.";


    /**
     * Creates a new video based on {@link CreateVideoDTO} object and associates it with a course.
     * The video's display order is adjusted if necessary to maintain sequence integrity.
     *
     * @param createVideoDTO DTO containing video creation details.
     * @param file           The video file to be uploaded.
     * @param requesterId    The UUID of the requester.
     * @return The created VideoDTO.
     * @throws IOException              If an error occurs during file upload.
     * @throws CourseNotFoundException  If the associated course is not found.
     * @throws InvalidFileTypeException If the uploaded file type is not allowed.
     * @throws UnauthorizedException    If the requester is not authorized to perform the operation.
     */
    @CacheEvict(value = "video", allEntries = true)
    public VideoDTO create(CreateVideoDTO createVideoDTO, MultipartFile file, UUID requesterId) throws IOException, CourseNotFoundException, InvalidFileTypeException, UnauthorizedException {

        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new InvalidFileTypeException(ALLOWED_FILE_TYPES);
        }

        Course course = this.courseRepository.findById(UUID.fromString(createVideoDTO.getCourseId()))
                .orElseThrow(CourseNotFoundException::new);

        if (isTheOwner(course.getId(), requesterId)) {
            return createProcess(createVideoDTO, course, file);
        }

        if (isAdmin(requesterId)) {
            return createProcess(createVideoDTO, course, file);
        }

        throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
    }

    /**
     * Processes the creation of a new video associated with the given course.
     *
     * @param createVideoDTO DTO containing video creation details.
     * @param course         The course to which the video belongs.
     * @param file           The video file to be uploaded.
     * @return The created VideoDTO.
     * @throws IOException If an error occurs during file upload.
     */
    private VideoDTO createProcess(CreateVideoDTO createVideoDTO, Course course, MultipartFile file) throws IOException {
        Video video = VideoMapper.INSTANCE.toVideo(createVideoDTO);
        video.setCourse(course);

        // Ensure the new order is within valid range
        int maxOrder = course.getVideos().size() + 1;
        if (video.getDisplayOrder() <= 0 || video.getDisplayOrder() > maxOrder) {
            throw new IllegalArgumentException("Invalid display order");
        }

        // Adjust the order of existing videos if necessary
        if (video.getDisplayOrder() <= course.getVideos().size()) {
            List<Video> videos = videoRepository.findAllByWithDisplayOrderBiggerThanOrEqual(video.getDisplayOrder());
            incrementDisplayOrder(videos);
        }

        video.setUrl(fileUploader.upload(file));

        return VideoMapper.INSTANCE.toVideoDTO(videoRepository.save(video));
    }

    /**
     * Finds all videos associated with a given course, ordered by their display order.
     *
     * @param courseId The UUID of the course.
     * @return A list of VideoDTOs.
     * @throws CourseNotFoundException If the course is not found.
     */
    @Cacheable(value = "video", key = "#courseId")
    public List<VideoDTO> findByCourse(UUID courseId) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        return this.videoRepository.findAllByCourseOrderByDisplayOrder(course).stream()
                .map(VideoMapper.INSTANCE::toVideoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a video by its ID.
     *
     * @param courseId    The UUID of the video to delete.
     * @param requesterId The UUID of the requester.
     * @return A success message.
     * @throws CourseNotFoundException If the video or course is not found.
     * @throws UnauthorizedException   If the requester is not authorized to perform the operation.
     */

    @CacheEvict(value = "video", allEntries = true)
    @Transactional
    public String delete(UUID courseId, UUID requesterId) throws CourseNotFoundException, UnauthorizedException {
        Video video = this.videoRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);

        if (isTheOwner(courseId, requesterId)) {
            return deleteProcess(video, courseId);
        }

        if (isAdmin(requesterId)) {
            return deleteProcess(video, courseId);
        }

        throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
    }

    /**
     * Processes the deletion of a video from the repository.
     *
     * @param video    The video entity to delete.
     * @param courseId The UUID of the video to delete.
     * @return A success message.
     */
    private String deleteProcess(Video video, UUID courseId) {
        this.videoRepository.delete(video);
        return "Video with id " + courseId + " deleted successfully.";
    }


    /**
     * Updates a video's details based on {@link UpdateVideoDTO} and re-uploads the video file.
     *
     * @param courseId       The UUID of the video to update.
     * @param updateVideoDTO DTO containing updated video details.
     * @param file           The new video file to be uploaded.
     * @param requesterId    The UUID of the requester.
     * @return The updated VideoDTO.
     * @throws CourseNotFoundException If the associated course is not found.
     * @throws VideoNotFoundException  If the video to update is not found.
     * @throws IOException             If an error occurs during file upload.
     * @throws UnauthorizedException   If the requester is not authorized to perform the operation.
     */
    @CacheEvict(value = "video", allEntries = true)
    public VideoDTO update(UUID courseId, UpdateVideoDTO updateVideoDTO, MultipartFile file, UUID requesterId) throws CourseNotFoundException, VideoNotFoundException, IOException, UnauthorizedException {
        Course course = this.courseRepository.findById(UUID.fromString(updateVideoDTO.getCourseId()))
                .orElseThrow(CourseNotFoundException::new);
        Video video = this.videoRepository.findById(courseId)
                .orElseThrow(VideoNotFoundException::new);

        if (isTheOwner(courseId, requesterId)) {
            return uploadProcess(video, course, updateVideoDTO, file);
        }

        if (isAdmin(requesterId)) {
            return uploadProcess(video, course, updateVideoDTO, file);
        }

        throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
    }

    /**
     * Processes the update of a video's details and re-uploads the video file.
     *
     * @param video          The video entity to update.
     * @param course         The course entity to associate with the video.
     * @param updateVideoDTO DTO containing updated video details.
     * @param file           The new video file to be uploaded.
     * @return The updated VideoDTO.
     * @throws IOException If an error occurs during file upload.
     */
    private VideoDTO uploadProcess(Video video, Course course, UpdateVideoDTO updateVideoDTO, MultipartFile file) throws IOException {
        video.setCourse(course);
        video.setTitle(updateVideoDTO.getTitle());
        video.setDurationInSeconds(updateVideoDTO.getDurationInSeconds());
        video.setUrl(fileUploader.upload(file));

        return VideoMapper.INSTANCE.toVideoDTO(videoRepository.save(video));
    }


    /**
     * Increments the display order of a list of videos.
     * This is a private helper method used to maintain the correct order of videos within a course.
     *
     * @param videos The list of videos whose display order needs to be incremented.
     */
    private void incrementDisplayOrder(List<Video> videos) {
        for (Video v : videos) {
            v.setDisplayOrder(v.getDisplayOrder() + 1);
            this.videoRepository.save(v);
        }
    }

    /**
     * Determines if the specified user has administrative privileges.
     *
     * @param requesterId The UUID of the user to check for administrative privileges.
     * @return true if the user has administrative privileges, false otherwise.
     */
    private boolean isAdmin(UUID requesterId) {
        return userRepository.isAdmin(requesterId);
    }

    /**
     * Checks if the specified user is the owner of the course.
     *
     * @param courseId    The UUID of the course to check ownership for.
     * @param requesterId The UUID of the user to verify as the owner.
     * @return true if the requesterId matches the owner of the course, false otherwise.
     */
    private boolean isTheOwner(UUID courseId, UUID requesterId) {
        Professor professor = professorRepository.findByUserId(requesterId);
        if (professor == null) return false;
        return courseRepository.isTheOwner(courseId, professor.getId());
    }

}
