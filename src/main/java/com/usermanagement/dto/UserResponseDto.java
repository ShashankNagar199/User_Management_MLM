package com.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserResponseDto {

    private Long userId;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    private String phone;

    @NotBlank(message = "referrer is required")
    private String referrer;

    private String rank;
}
