package org.wiliammelo.empoweru.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.course.CreateCourseDTO;
import org.wiliammelo.empoweru.dtos.course.UpdateCourseDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Course;
import org.wiliammelo.empoweru.models.CustomResponse;
import org.wiliammelo.empoweru.services.CourseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable("id") UUID id)throws CourseNotFoundException {
        return new ResponseEntity<>(this.courseService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Course>> findCourses(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tags", required = false) List<String> tags
    ) {
        List<Course> courses;

        if (title != null && !title.isEmpty() && tags != null && !tags.isEmpty()) {
            // Buscar por título e tags
            courses = courseService.findByTitleAndTags(title, tags);
        } else if (title != null && !title.isEmpty()) {
            // Buscar somente por título
            courses = courseService.findByTitle(title);
        } else if (tags != null && !tags.isEmpty()) {
            // Buscar somente por tags
            courses = courseService.findByTags(tags);
        } else {
            // Se nenhum parâmetro for fornecido, retornar todos os cursos
            courses = courseService.findAll();
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseDTO createCourseDTO) throws UserNotFoundException {
        return new ResponseEntity<>(this.courseService.create(createCourseDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateById(@PathVariable("id") UUID id, @RequestBody UpdateCourseDTO updateCourseDTO) throws CourseNotFoundException {
        return new ResponseEntity<>(this.courseService.update(id, updateCourseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> removeById(@PathVariable("id") UUID id) throws CourseNotFoundException {
        return new ResponseEntity<>(new CustomResponse(this.courseService.delete(id), HttpStatus.OK.value()), HttpStatus.OK);
    }


}
