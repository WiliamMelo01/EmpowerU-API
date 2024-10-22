package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.student.StudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<List<StudentDTO>> getAll() {
        return new ResponseEntity<>(this.studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteById(@AuthenticationPrincipal User user) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.deleteById(user.getId()), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<StudentDTO> update(@RequestBody @Valid UpdateStudentDTO updateStudentDTO, @AuthenticationPrincipal User user) throws UserNotFoundException {
        return new ResponseEntity<>(this.studentService.update(user.getId(), updateStudentDTO), HttpStatus.OK);
    }

}
