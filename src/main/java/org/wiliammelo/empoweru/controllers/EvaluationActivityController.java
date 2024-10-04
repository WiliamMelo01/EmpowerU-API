package org.wiliammelo.empoweru.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateEvaluationActivityDTO;
import org.wiliammelo.empoweru.dtos.evaluation_activity_result.CreateEvaluationActivityResultDTO;
import org.wiliammelo.empoweru.exceptions.*;
import org.wiliammelo.empoweru.models.EvaluationActivity;
import org.wiliammelo.empoweru.models.EvaluationActivityResult;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.services.EvaluationActivityResultService;
import org.wiliammelo.empoweru.services.EvaluationActivityService;

@RestController
@RequestMapping("/evaluation-activities")
@AllArgsConstructor
public class EvaluationActivityController {

    private final EvaluationActivityService evaluationActivityService;
    private final EvaluationActivityResultService evaluationActivityResultService;

    @PostMapping("/")
    public ResponseEntity<EvaluationActivity> create(@RequestBody @Validated CreateEvaluationActivityDTO createEvaluationActivityDTO, @AuthenticationPrincipal User user) throws ProfessorNotFoundException, SectionNotFoundException, UnauthorizedException, EvaluationAlreadyExistsException {
        return new ResponseEntity<>(this.evaluationActivityService.create(createEvaluationActivityDTO, user.getId()), HttpStatus.CREATED);
    }

    @PostMapping("/result/")
    public ResponseEntity<EvaluationActivityResult> result(@RequestBody @Validated CreateEvaluationActivityResultDTO createEvaluationActivityResultDTO) throws EvaluationActivityNotFoundException, CourseNotFoundException, StudentNotFoundException {
        return new ResponseEntity<>(this.evaluationActivityResultService.create(createEvaluationActivityResultDTO), HttpStatus.CREATED);
    }

}
