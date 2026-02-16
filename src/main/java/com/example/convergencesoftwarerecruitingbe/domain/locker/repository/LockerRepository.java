package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    List<Locker> findAllByStatusAndIsFixedFalse(LockerStatus status);

    List<Locker> findAllByOrderByRowNoAscColNoAsc();

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Locker> findById(Long id);

    @Query("SELECT l.status as status, COUNT(l) as count FROM Locker l GROUP BY l.status")
    List<LockerStatusCount> countByStatus();

    @Modifying
    @Query("UPDATE Locker l SET l.status = :status WHERE l.id > 0")
    void updateAllStatus(@Param("status") LockerStatus status);

    interface LockerStatusCount {
        LockerStatus getStatus();

        Long getCount();
    }
}
