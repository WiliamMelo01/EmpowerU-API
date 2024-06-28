package org.wiliammelo.empoweru.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Student;
import org.wiliammelo.empoweru.services.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/")
    public ResponseEntity<Student> create(@RequestBody CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.studentService.create(createStudentDTO), HttpStatus.CREATED);
    }


    @GetMapping
    public  ResponseEntity<List<Student>> getAll(){
        return new ResponseEntity<>(this.studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Student> getById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) throws UserNotFoundException {
       return new ResponseEntity<>(this.studentService.deleteById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable UUID id, @RequestBody UpdateStudentDTO updateStudentDTO) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.update(id, updateStudentDTO), HttpStatus.OK);
    }


}
