package org.wiliammelo.empoweru.dtos.evaluation_activity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateQuestionOptionDTO {
    @Length(min = 1, max = 255, message = "Option text must be between 1 and 255 characters.")
    private String text;

    @NotNull(message = "Option must be marked as correct or incorrect.")
    @Pattern(regexp = "^true$|^false$", message = "allowed input: true or false")
    private String correct;
}
