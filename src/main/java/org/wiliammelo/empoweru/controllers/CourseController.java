package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<Object> findById(@PathVariable("id") UUID id) throws CourseNotFoundException, UserNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(this.courseService.findByIdAuthenticated(id, user.getId()), HttpStatus.OK);
    }

    @GetMapping("/public/")
    public ResponseEntity<List<CourseDTO>> findAll(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tags", required = false) List<String> tags
    ) {
        List<CourseDTO> courses;

        if (title != null && !title.isEmpty() && tags != null && !tags.isEmpty()) {
            // Find by tags and title
            courses = courseService.findByTitleAndTags(title, tags);
        } else if (title != null && !title.isEmpty()) {
            // Find only by title
            courses = courseService.findByTitle(title);
        } else if (tags != null && !tags.isEmpty()) {
            // Find only by tags
            courses = courseService.findByTags(tags);
        } else {
            // If none parameter is given returns all courses
            courses = courseService.findAll();
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CourseDTO> create(@Valid @RequestBody CreateCourseDTO createCourseDTO) throws UserNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(this.courseService.create(createCourseDTO, user.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable("id") UUID courseId, @RequestBody @Valid UpdateCourseDTO updateCourseDTO) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(this.courseService.update(courseId, updateCourseDTO, user.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> remove(@PathVariable("id") UUID courseId) throws CourseNotFoundException, UnauthorizedException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(new CustomResponse(this.courseService.delete(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping("/enroll/{id}")
    public ResponseEntity<CustomResponse> enroll(@PathVariable("id") UUID courseId) throws UserNotFoundException, CourseNotFoundException, UserAlreadyEnrolledException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(new CustomResponse(this.courseService.enroll(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping("/disenroll/{id}")
    public ResponseEntity<CustomResponse> disenroll(@PathVariable("id") UUID courseId) throws UserNotFoundException, CourseNotFoundException, UserNotEnrolledException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(new CustomResponse(this.courseService.disenroll(courseId, user.getId()), HttpStatus.OK.value()), HttpStatus.OK);
    }

}
