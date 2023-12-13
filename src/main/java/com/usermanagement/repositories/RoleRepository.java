package com.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usermanagement.models.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{

}
