package org.wiliammelo.empoweru.dtos.student;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateStudentDTO {

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    public String name;

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    public String email;

    @NotNull(message = "Cannot be null.")
    @Pattern(regexp = "[FM]", message = "Gender must be 'F' or 'M'.")
    public String gender;

}
