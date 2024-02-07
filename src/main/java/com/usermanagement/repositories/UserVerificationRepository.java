package com.usermanagement.repositories;

import com.usermanagement.models.User;
import com.usermanagement.models.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    Optional<UserVerification> findByUserId(String userId);

    @Query(value = "select * from user_verification where user_id=:userId", nativeQuery = true)
    UserVerification getByUserId(String userId);
}
