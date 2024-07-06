package org.wiliammelo.empoweru.dtos.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentDTO {

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    public String name;

    @NotNull(message = "Cannot be null.")
    @Pattern(regexp = "[FM]", message = "Gender must be 'F' or 'M'.")
    public String gender;

}
