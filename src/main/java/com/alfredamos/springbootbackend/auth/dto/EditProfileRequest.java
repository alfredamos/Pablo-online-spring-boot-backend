package com.alfredamos.springbootbackend.auth.dto;

import com.alfredamos.springbootbackend.users.Gender;
import com.alfredamos.springbootbackend.users.Role;
import com.alfredamos.springbootbackend.validations.ValueOfEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditProfileRequest {
    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Phone is required.")
    private String phone;

    @ValueOfEnum(enumClass = Gender.class, message = "It must be either Male of Female!")
    private Gender gender;

    @NotBlank(message = "Image is required.")
    private String image;

    @NotBlank(message = "Password is required.")
    private String password;

    @ValueOfEnum(enumClass = Role.class, message = "Selection is not in the enum list!")
    private Role role;
}
