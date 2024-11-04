package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.CustomResponse;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.*;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.CourseService;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/public/{id}")
    public ResponseEntity<Object> findByIdPublic(
            @PathVariable("id") UUID id,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDetails) throws CourseNotFoundException {
        return new ResponseEntity<>(this.courseService.findByIdPublic(id, includeDetails), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") UUID id, @AuthenticationPrincipal User user) throws CourseNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(this.courseService.findByIdAuthenticated(id, user.getId()), HttpStatus.OK);
    }

    @GetMapping("/public/")
    public ResponseEntity<List<CourseDTO>> findAll(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "page_size", defaultValue = "15", required = false) int pageSize
    ) {
        List<CourseDTO> courses;

        if (title != null && !title.isEmpty() && tags != null && !tags.isEmpty()) {
            // Find by tags and title
            courses = courseService.findByTitleAndTags(title, tags, page, pageSize);
        } else if (title != null && !title.isEmpty()) {
            // Find only by title
            courses = courseService.findByTitle(title, page, pageSize);
        } else if (tags != null && !tags.isEmpty()) {
            // Find only by tags
            courses = courseService.findByTags(tags, page, pageSize);
        } else {
            // If none parameter is given returns all courses
            courses = courseService.findAll(page, pageSize);
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CourseDTO> create(@Valid @RequestBody CreateCourseDTO createCourseDTO, @AuthenticationPrincipal User user) throws UserNotFoundException {
        return new ResponseEntity<>(this.courseService.create(createCourseDTO, user.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable("id") UUID courseId,
                                            @RequestBody @Valid UpdateCourseDTO updateCourseDTO,
                                            @AuthenticationPrincipal User user) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        return new ResponseEntity<>(this.courseService.update(courseId, updateCourseDTO, user.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> remove(@PathVariable("id") UUID courseId, @AuthenticationPrincipal User user) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        return new ResponseEntity<>(new CustomResponse(this.courseService.delete(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping("/enroll/{id}")
    public ResponseEntity<CustomResponse> enroll(@PathVariable("id") UUID courseId, @AuthenticationPrincipal User user) throws UserNotFoundException, CourseNotFoundException, UserAlreadyEnrolledException {
        return new ResponseEntity<>(new CustomResponse(this.courseService.enroll(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping("/disenroll/{id}")
    public ResponseEntity<CustomResponse> disenroll(@PathVariable("id") UUID courseId, @AuthenticationPrincipal User user) throws UserNotFoundException, CourseNotFoundException, UserNotEnrolledException {
        return new ResponseEntity<>(new CustomResponse(this.courseService.disenroll(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

}
