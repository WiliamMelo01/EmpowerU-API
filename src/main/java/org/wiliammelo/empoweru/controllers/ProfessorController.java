package org.wiliammelo.empoweru.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.professor.CreateProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.ProfessorDTO;
import org.wiliammelo.empoweru.dtos.professor.UpdateProfessorDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.Professor;
import org.wiliammelo.empoweru.services.ProfessorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/")
    public ResponseEntity<ProfessorDTO> create(@RequestBody CreateProfessorDTO createProfessorDTO) throws UserAlreadyExistsException {
        return new ResponseEntity<>(this.professorService.create(createProfessorDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<List<ProfessorDTO>> getAll(){
        return new ResponseEntity<>(this.professorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ProfessorDTO> getById(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.professorService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) throws UserNotFoundException {
        return new ResponseEntity<>(this.professorService.deleteById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> update(@PathVariable UUID id, @RequestBody UpdateProfessorDTO updateStudentDTO) throws UserNotFoundException {
        return new ResponseEntity<>(this.professorService.update(id, updateStudentDTO), HttpStatus.OK);
    }

}
