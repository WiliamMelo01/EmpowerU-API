package org.wiliammelo.empoweru.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wiliammelo.empoweru.dtos.section.CreateSectionDTO;
import org.wiliammelo.empoweru.dtos.section.SectionDTO;
import org.wiliammelo.empoweru.dtos.section.UpdateSectionDTO;
import org.wiliammelo.empoweru.exceptions.CourseNotFoundException;
import org.wiliammelo.empoweru.exceptions.ProfessorNotFoundException;
import org.wiliammelo.empoweru.exceptions.SectionNotFoundException;
import org.wiliammelo.empoweru.exceptions.UnauthorizedException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.SectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/section")
@AllArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/")
    public ResponseEntity<SectionDTO> create(@RequestBody @Valid CreateSectionDTO createSectionDTO) throws UnauthorizedException, CourseNotFoundException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(sectionService.create(createSectionDTO, user.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<List<SectionDTO>> findAllByCourse(@PathVariable UUID courseId) throws CourseNotFoundException {
        return new ResponseEntity<>(sectionService.findAllByCourse(courseId), HttpStatus.OK);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<String> delete(@PathVariable UUID sectionId) throws UnauthorizedException, SectionNotFoundException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(sectionService.delete(sectionId, user.getId()), HttpStatus.OK);
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionDTO> update(@PathVariable UUID sectionId, @RequestBody @Valid UpdateSectionDTO updateSectionDTO) throws UnauthorizedException, SectionNotFoundException, ProfessorNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(sectionService.update(sectionId, updateSectionDTO, user.getId()), HttpStatus.OK);
    }


}
