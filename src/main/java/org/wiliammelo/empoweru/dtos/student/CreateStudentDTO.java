package org.wiliammelo.empoweru.dtos.student;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentDTO {

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    public String name;

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Size(min = 2, max = 100, message = "Email must be between 2 and 100 characters.")
    public String email;

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must be between 6 and 50 characters.")
    @Size(max = 100, message = "Password must be between 6 and 50 characters.")
    public String password;

    @NotNull(message = "Cannot be null.")
    @Pattern(regexp = "[FM]", message = "Gender must be 'F' or 'M'.")
    public String gender;

}
