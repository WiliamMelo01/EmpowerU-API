package org.wiliammelo.empoweru.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.wiliammelo.empoweru.dtos.evaluation_activity.CreateQuestionDTO;

public class OneRightOptionValidator implements ConstraintValidator<OneRightOption, CreateQuestionDTO> {

    @Override
    public boolean isValid(CreateQuestionDTO createQuestionDTOS, ConstraintValidatorContext constraintValidatorContext) {
        if (createQuestionDTOS == null || createQuestionDTOS.getOptions() == null) {
            return true;
        }

        long correctAnswersCount = createQuestionDTOS.getOptions().stream()
                .filter(option -> "true".equals(option.getCorrect()))
                .count();

        return correctAnswersCount == 1;
    }
}
