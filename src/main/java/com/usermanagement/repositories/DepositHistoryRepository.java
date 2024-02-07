package com.usermanagement.repositories;

import com.usermanagement.models.DepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long> {
}
