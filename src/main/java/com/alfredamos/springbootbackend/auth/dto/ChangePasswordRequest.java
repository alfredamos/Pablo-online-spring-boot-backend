package com.alfredamos.springbootbackend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "OldPassword is required.")
    private String oldPassword;

    @NotBlank(message = "ConfirmPassword is required.")
    private String confirmPassword;

    @NotBlank(message = "newPassword is required.")
    private String newPassword;
}
