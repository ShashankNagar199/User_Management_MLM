package com.usermanagement.dto;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserResponseDto {

    private Long userId;

    private String email;

    private String password;

    private String phone;

    private String referrer;

    private String rank;
}
