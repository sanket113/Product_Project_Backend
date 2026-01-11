package com.example.rbac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDto {

    @Email
    @NotBlank
    private String email;
}
