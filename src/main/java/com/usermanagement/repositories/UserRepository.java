package com.usermanagement.repositories;

import com.usermanagement.dto.ReferralTreeResponse;
import com.usermanagement.dto.TotalSalesByChildren;
import com.usermanagement.dto.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usermanagement.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where email=:email", nativeQuery = true)
	User findByEmailId(String email);

    Optional<User> findByEmail(String email);

    @Query(value = "select * from users where user_id=:userId", nativeQuery = true)
    User findByUserId(@Param("userId")  String userId);

    @Query(value = "select * from users where referrer_id=:userId", nativeQuery = true)
    List<User>  getAllChildrenByUserId(@Param("userId") Long userId);

    @Query(value = "select count(*) from users", nativeQuery = true)
    UserCount  getCountOfAllUsers();

    @Query(value = "select SUM(sales) from users where referrer_id=:userId", nativeQuery = true)
    TotalSalesByChildren getSalesOfChildrenNodesByUserId(@Param("userId") String userId);

    @Query(value = "select * from users where wallet_address=:walletAddress", nativeQuery = true)
    User findByWalletAddress(@Param("walletAddress") String walletAddress);
    @Query(nativeQuery = true,
            value = "WITH RECURSIVE ReferralTree AS (" +
                    "  SELECT u.user_id, u.referrer, 1 AS level " +
                    "  FROM users u " +
                    "  WHERE u.user_id = :userId " +
                    "  UNION ALL " +
                    "  SELECT u.user_id, u.referrer, rt.level + 1 " +
                    "  FROM users u " +
                    "  JOIN ReferralTree rt ON u.referrer = rt.user_id" +
                    ") " +
                    "SELECT rt.user_id, rt.level " +
                    "FROM ReferralTree rt " +
                    "WHERE rt.referrer IS NOT NULL "+
                    " order by rt.level")
    List<ReferralTreeResponse> findReferralTree(@Param("userId") String userId);

    @Query(nativeQuery = true,
            value =   "WITH RECURSIVE UserHierarchy AS ("+
            " SELECT user_id, referrer, level "+
            " FROM users "+
            " WHERE user_id = :userId "+
            " UNION ALL "+
            " SELECT u.user_id, u.referrer, u.level "+
            " FROM users u "+
            " JOIN UserHierarchy h ON u.user_id = h.referrer "+
    ") "+
            " SELECT referrer, :level-level AS level_difference "+
            " FROM UserHierarchy "+
            " WHERE referrer IS NOT NULL "+
            " ORDER BY level")
    List<ReferralTreeResponse> findReferralNodeTree(@Param("userId") String userId,@Param("level") Integer level);

}
