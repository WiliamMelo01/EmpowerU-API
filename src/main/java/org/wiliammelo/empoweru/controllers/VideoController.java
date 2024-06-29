package org.wiliammelo.empoweru.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wiliammelo.empoweru.dtos.video.CreateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.UpdateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.VideoNotFoundException;
import org.wiliammelo.empoweru.models.CustomResponse;
import org.wiliammelo.empoweru.services.VideoService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping(value = "/upload", consumes = {
            "multipart/form-data"
    },produces = {
            "application/json"
    })
    public ResponseEntity<?> create(@RequestPart("video") CreateVideoDTO createVideoDTO,
                                         @RequestPart("file") MultipartFile file){
        try {
            return new ResponseEntity<>(this.videoService.create(createVideoDTO, file), HttpStatus.CREATED);
        }catch (CourseNotFoundException cnf){
            return new ResponseEntity<>(new CustomResponse(cnf.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }catch (IOException io){
            return new ResponseEntity<>(new CustomResponse("Error while saving video.Verify the file or try again later.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            return new ResponseEntity<>(new CustomResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<List<VideoDTO>> findByCourseId(@PathVariable("courseId") UUID courseId) throws CourseNotFoundException {
            return new ResponseEntity<>(this.videoService.findByCourse(courseId), HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<CustomResponse> delete(@PathVariable("videoId") UUID courseId) throws CourseNotFoundException {

      String message =  this.videoService.delete(courseId);

      return new ResponseEntity<>(new CustomResponse(message, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDTO> update(@RequestPart("video") UpdateVideoDTO updateVideoDTO,
                                                 @RequestPart("file") MultipartFile file,
                                                 @PathVariable("videoId") UUID courseId) throws CourseNotFoundException, VideoNotFoundException,IOException {

        VideoDTO video =  this.videoService.update(courseId, updateVideoDTO, file);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }
}
