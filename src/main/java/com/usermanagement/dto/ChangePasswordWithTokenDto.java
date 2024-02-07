package com.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordWithTokenDto {
    @NotBlank(message = "token of the user is mandatory")
    private String token;
    @NotBlank(message = "new password of the user is mandatory")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
