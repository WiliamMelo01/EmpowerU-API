package org.wiliammelo.empoweru.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginDTO {

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Size(min = 2, max = 100, message = "Email must be between 2 and 100 characters.")
    private String email;

    @NotNull(message = "Cannot be null.")
    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must be between 6 and 50 characters.")
    @Size(max = 100, message = "Password must be between 6 and 50 characters.")
    private String password;

}
