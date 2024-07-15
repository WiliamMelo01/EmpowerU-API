package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.configuration.security.JWTService;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.ProfessorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professor")
@AllArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/")
    public ResponseEntity<List<ProfessorDTO>> getAll() {
        return new ResponseEntity<>(this.professorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.professorService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> delete() throws UserNotFoundException {
        User user = JWTService.getUserFromAuthContext();
        return new ResponseEntity<>(this.professorService.delete(user.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.professorService.deleteById(id), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<ProfessorDTO> update(@RequestBody @Valid UpdateProfessorDTO updateStudentDTO) throws UserNotFoundException {
        User user = JWTService.getUserFromAuthContext();
        return new ResponseEntity<>(this.professorService.update(user.getId(), updateStudentDTO), HttpStatus.OK);
    }

}
