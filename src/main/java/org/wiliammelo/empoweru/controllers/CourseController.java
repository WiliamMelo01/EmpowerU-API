package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.CustomResponse;
import org.wiliammelo.empoweru.dtos.course.CourseDTO;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.services.CourseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> findById(@PathVariable("id") UUID id) throws CourseNotFoundException {
        return new ResponseEntity<>(this.courseService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/")
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
        return new ResponseEntity<>(this.courseService.create(createCourseDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid UpdateCourseDTO updateCourseDTO) throws CourseNotFoundException {
        return new ResponseEntity<>(this.courseService.update(id, updateCourseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> remove(@PathVariable("id") UUID id) throws CourseNotFoundException {
        return new ResponseEntity<>(new CustomResponse(this.courseService.delete(id), HttpStatus.OK.value()), HttpStatus.OK);
    }


}
