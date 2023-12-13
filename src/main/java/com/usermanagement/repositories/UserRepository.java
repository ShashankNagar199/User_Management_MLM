package com.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usermanagement.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
