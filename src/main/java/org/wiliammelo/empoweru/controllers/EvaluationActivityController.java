package org.wiliammelo.empoweru.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateEvaluationActivityDTO;
import org.wiliammelo.empoweru.exceptions.EvaluationAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.ProfessorNotFoundException;
import org.wiliammelo.empoweru.exceptions.SectionNotFoundException;
import org.wiliammelo.empoweru.exceptions.UnauthorizedException;
import org.wiliammelo.empoweru.models.EvaluationActivity;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.EvaluationActivityService;

@RestController
@RequestMapping("/evaluation-activities")
@AllArgsConstructor
public class EvaluationActivityController {

    private final EvaluationActivityService evaluationActivityService;

    @PostMapping("/")
    public ResponseEntity<EvaluationActivity> create(@RequestBody @Validated CreateEvaluationActivityDTO createEvaluationActivityDTO) throws ProfessorNotFoundException, SectionNotFoundException, UnauthorizedException, EvaluationAlreadyExistsException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(this.evaluationActivityService.create(createEvaluationActivityDTO, user.getId()), HttpStatus.CREATED);
    }

}
