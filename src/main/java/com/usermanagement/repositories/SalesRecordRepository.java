package com.usermanagement.repositories;

import com.usermanagement.models.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    @Query(value = "select * from sales_records where user_id=:userId", nativeQuery = true)
    SalesRecord findBySalesId(@Param("userId")  String userId);
}
