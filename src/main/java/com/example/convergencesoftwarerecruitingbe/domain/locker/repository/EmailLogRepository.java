package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.EmailLog;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findAllByApplicationIdOrderBySentAtDesc(Long applicationId);

    List<EmailLog> findAllByStatusAndSentAtBefore(EmailStatus status, LocalDateTime before);

    @Query("SELECT e.emailType as type, e.status as status, COUNT(e) as count " +
            "FROM EmailLog e GROUP BY e.emailType, e.status")
    List<EmailTypeStatusCount> countByTypeAndStatus();

    List<EmailLog> findAllByToEmailOrderBySentAtDesc(String toEmail);

    interface EmailTypeStatusCount {
        EmailType getType();

        EmailStatus getStatus();

        Long getCount();
    }
}
