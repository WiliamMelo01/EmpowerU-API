package org.wiliammelo.empoweru.dtos.professor;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateProfessorDTO {
    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
    @Size(min = 2, message = "Name must be between 2 and 100 characters.")
    @Size(max = 100, message = "Name must be between 2 and 100 characters.")
    public String name;

    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    public String email;

    @NotNull(message = "Gender is required.")
    @NotBlank(message = "Gender is required.")
    @Pattern(regexp = "^[MF]$", message = "Gender must be 'M' or 'F'.")
    public String gender;

    @NotNull(message = "Bio is required.")
    @NotBlank(message = "Bio is required.")
    @Size(max = 500, message = "Bio must be at most 500 characters.")
    @Size(min = 100, message = "Bio must be at least 100 characters.")
    private String bio;

    @NotNull(message = "ImageUrl is required.")
    @NotBlank(message = "ImageUrl is required.")
    @Size(max = 500, message = "ImageUrl must be at most 500 characters.")
    @Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)", message = "ImageUrl must be a valid URL to an image.")
    private String imageUrl;
}
