package org.wiliammelo.empoweru.dtos.evaluation_activity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateEvaluationActivityDTO {

    @org.hibernate.validator.constraints.UUID(message = "Section ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    private String sectionId;

    @NotNull(message = "At least 10 questions are required.")
    @Size(min = 10, message = "At least 10 questions are required.")
    @Size(max = 15, message = "A maximum of 15 questions are required.")
    @Valid
    private List<CreateQuestionDTO> questions;

}
