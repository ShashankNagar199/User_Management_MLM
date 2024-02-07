package com.usermanagement.repositories;

import com.usermanagement.models.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRankRepository extends JpaRepository<UserRank, Long> {
    @Query(value = "select * from user_rank where id=:id", nativeQuery = true)
    UserRank findUserRankById(@Param("userId") Long id);
}
