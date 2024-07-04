package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.services.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/")
    public ResponseEntity<StudentDTO> create(@Valid @RequestBody CreateStudentDTO createStudentDTO) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.studentService.create(createStudentDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return new ResponseEntity<>(this.studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.deleteById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@PathVariable UUID id, @RequestBody @Valid UpdateStudentDTO updateStudentDTO) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.update(id, updateStudentDTO), HttpStatus.OK);
    }

}
