package com.usermanagement.dto;


import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequestDto {
    @NotBlank(message = "userId of the user is mandatory")
    private String userId;
    @NotBlank(message = "email of the user is mandatory")
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
