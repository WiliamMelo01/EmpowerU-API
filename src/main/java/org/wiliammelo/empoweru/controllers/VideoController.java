package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wiliammelo.empoweru.dtos.CustomResponse;
import org.wiliammelo.empoweru.dtos.video.CreateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.UpdateVideoDTO;
import org.wiliammelo.empoweru.dtos.video.VideoDTO;
import org.wiliammelo.empoweru.exceptions.*;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.models.VideoWatched;
import org.wiliammelo.empoweru.services.VideoService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/video")
@AllArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(value = "/upload", consumes = {
            "multipart/form-data"
    }, produces = {
            "application/json"
    })
    public ResponseEntity<Object> create(@RequestPart("video") @Valid CreateVideoDTO createVideoDTO,
                                         @RequestPart("file") MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            return new ResponseEntity<>(this.videoService.create(createVideoDTO, file, user.getId()), HttpStatus.CREATED);
        } catch (SectionNotFoundException sectionNotFoundException) {
            return new ResponseEntity<>(new CustomResponse(sectionNotFoundException.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (IOException ioException) {
            return new ResponseEntity<>(new CustomResponse("Error while saving video.Verify the file or try again later.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidFileTypeException invalidFileTypeException) {
            return new ResponseEntity<>(new CustomResponse(invalidFileTypeException.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException unauthorizedException) {
            return new ResponseEntity<>(new CustomResponse(unauthorizedException.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<CustomResponse> delete(@PathVariable("videoId") UUID courseId) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String message = this.videoService.delete(courseId, user.getId());

        return new ResponseEntity<>(new CustomResponse(message, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDTO> update(@RequestPart("video") @Valid UpdateVideoDTO updateVideoDTO,
                                           @RequestPart("file") MultipartFile file,
                                           @PathVariable("videoId") UUID courseId) throws SectionNotFoundException, VideoNotFoundException, IOException, UnauthorizedException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VideoDTO video = this.videoService.update(courseId, updateVideoDTO, file, user.getId());

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @PostMapping("/mark-as-watched/{id}")
    public ResponseEntity<VideoWatched> markAsWatched(@PathVariable("id") UUID videoId) throws UserNotFoundException, VideoNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(this.videoService.markAsWatched(videoId, user.getId()), HttpStatus.OK);
    }

}
