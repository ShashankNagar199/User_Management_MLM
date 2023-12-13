package com.usermanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usermanagement.models.Referral;
import com.usermanagement.models.User;

public interface ReferralRepository extends JpaRepository<Referral, Long>{

	List<Referral> findByReferrerOrReferredUser(User user);

}
