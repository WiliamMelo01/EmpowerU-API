package org.wiliammelo.empoweru.dtos.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateCourseDTO {

    @NotBlank(message = "Title is required.")
    @NotNull(message = "Title is required.")
    @Size(max = 255, message = "Title must have at most 255 characters.")
    @Size(min = 10, message = "Title must have at least 10 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @NotNull(message = "Description is required.")
    @Size(max = 1000, message = "Description must have at most 1000 characters.")
    @Size(min = 50, message = "Description must have at least 50 characters.")
    private String description;

    @NotNull(message = "At least one tag is required.")
    @Size(min = 1, message = "At least one tag is required.")
    @Size(max = 10, message = "At least most tag is required.")
    private List<String> tags;

    @org.hibernate.validator.constraints.UUID(message = "Professor ID must be a valid UUID.", allowEmpty = false, allowNil = false)
    @NotNull(message = "Professor ID is required.")
    private String professorId;
}
