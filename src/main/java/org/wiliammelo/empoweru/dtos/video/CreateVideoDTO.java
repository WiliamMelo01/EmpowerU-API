package org.wiliammelo.empoweru.dtos.video;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVideoDTO {

    @NotBlank(message = "Title is required.")
    @NotNull(message = "Title is required.")
    @Size(max = 255, message = "Title must have at most 255 characters.")
    @Size(min = 10, message = "Title must have at least 10 characters.")
    private String title;

    @org.hibernate.validator.constraints.UUID(message = "Course ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    @NotNull(message = "Section ID is required.")
    private String sectionId;

    @NotNull(message = "Display order is required.")
    @Min(value = 1, message = "Display order must be at least 1.")
    private int displayOrder;

    @NotNull(message = "Duration in seconds is required.")
    @Min(value = 300, message = "Duration in seconds must be at least 300 (5 minutes).")
    private double durationInSeconds;

}
