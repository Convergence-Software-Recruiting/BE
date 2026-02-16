package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

    Optional<SystemConfig> findFirstByOrderByCreatedAtDesc();

    @Query("SELECT s.applicationOpen FROM SystemConfig s WHERE s.createdAt = (SELECT MAX(sc.createdAt) FROM SystemConfig sc)")
    Optional<Boolean> isApplicationOpen();
}
