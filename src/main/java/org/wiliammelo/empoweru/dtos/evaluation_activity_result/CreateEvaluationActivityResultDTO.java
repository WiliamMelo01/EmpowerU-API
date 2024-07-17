package org.wiliammelo.empoweru.dtos.evaluation_activity_result;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateEvaluationActivityResultDTO {

    @org.hibernate.validator.constraints.UUID(message = "Evaluation ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    private String evaluationId;

    @org.hibernate.validator.constraints.UUID(message = "Course ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    private String courseId;

    @org.hibernate.validator.constraints.UUID(message = "Student ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    private String studentId;

    @Max(value = 10, message = "Grade must be a number between 0 and 10.")
    @Min(value = 0, message = "Grade must be a number between 0 and 10.")
    private float grade;

}
