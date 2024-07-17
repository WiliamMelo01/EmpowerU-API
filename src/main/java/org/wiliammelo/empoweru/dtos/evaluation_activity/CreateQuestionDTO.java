package org.wiliammelo.empoweru.dtos.evaluation_activity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.wiliammelo.empoweru.validation.OneRightOption;

import java.util.List;

@Data
@OneRightOption(message = "Each question must have exactly one correct answer")
public class CreateQuestionDTO {
    @NotNull(message = "Question text is required.")
    @NotBlank(message = "Question text is required.")
    @Length(min = 1, max = 255, message = "Question text must be between 1 and 255 characters.")
    private String text;

    @NotNull(message = "At least 5 options are required.")
    @Size(min = 5, max = 5, message = "Exactly 5 options are required.")
    @Valid
    private List<CreateQuestionOptionDTO> options;

}
