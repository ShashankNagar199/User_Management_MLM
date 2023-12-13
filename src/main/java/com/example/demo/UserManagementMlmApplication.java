package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


@SpringBootApplication
public class UserManagementMlmApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementMlmApplication.class, args);
	}

}
