package org.wiliammelo.empoweru.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wiliammelo.empoweru.dtos.video.CreateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.UpdateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.VideoNotFoundException;
import org.wiliammelo.empoweru.fileUpload.FileUploader;
import org.wiliammelo.empoweru.mappers.VideoMapper;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.Video;
import org.wiliammelo.empoweru.repositories.CourseRepository;
import org.wiliammelo.empoweru.repositories.VideoRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileUploader fileUploader;

    @Autowired
    private CourseRepository courseRepository;

    public VideoDTO create(CreateVideoDTO createVideoDTO, MultipartFile file) throws IOException, CourseNotFoundException {
        Course course = this.courseRepository.findById(createVideoDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);

        Video video = VideoMapper.INSTANCE.toVideo(createVideoDTO);
        video.setCourse(course);

        int newOrder = video.getDisplayOrder();
        int maxOrder = course.getVideos().size() + 1;

        if (newOrder <= 0 || newOrder > maxOrder) {
            throw new IllegalArgumentException("Invalid display order");
        }

        // Ajustar a ordem dos vídeos
        if (newOrder <= course.getVideos().size()) {
            List<Video> videos = this.videoRepository.findAllByWithDisplayOrderBiggerThanOrEqual(video.getDisplayOrder());
            this.incrementDisplayOrder(videos);
        }

        String videoURl = fileUploader.upload(file);
        video.setUrl(videoURl);

        Video createdVideo = this.videoRepository.save(video);
        return VideoMapper.INSTANCE.toVideoDTO(createdVideo);
    }

    public List<VideoDTO> findByCourse(UUID courseId) throws CourseNotFoundException {
        Course course = this.courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);

        return this.videoRepository.findAllByCourseOrderByDisplayOrder(course).stream().map(VideoMapper.INSTANCE::toVideoDTO).toList();
    }

    @Transactional
    public String delete(UUID courseId)throws CourseNotFoundException{
        Video video = this.videoRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        this.videoRepository.delete(video);
        return "Video deleted successfully.";
    }

    public VideoDTO update(UUID courseId, UpdateVideoDTO updateVideoDTO, MultipartFile file) throws CourseNotFoundException, VideoNotFoundException, IOException {

        Course course = this.courseRepository.findById(updateVideoDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);
        Video video = this.videoRepository.findById(courseId).orElseThrow(VideoNotFoundException::new);

        video.setCourse(course);
        video.setTitle(updateVideoDTO.getTitle());
        video.setDurationInSeconds(updateVideoDTO.getDurationInSeconds());

        String videoURl = fileUploader.upload(file);
        video.setUrl(videoURl);

        Video createdVideo = this.videoRepository.save(video);
        return VideoMapper.INSTANCE.toVideoDTO(createdVideo);
    }

    private void incrementDisplayOrder(List<Video> videos){
        for (Video v : videos) {
            v.setDisplayOrder(v.getDisplayOrder() + 1);
            this.videoRepository.save(v);
        }
    }

}
