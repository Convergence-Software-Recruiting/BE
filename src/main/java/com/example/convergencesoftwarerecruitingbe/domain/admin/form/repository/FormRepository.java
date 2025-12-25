package com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findFirstByActiveTrue();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Form f set f.active=false where f.active=true")
    int deactivateAllActive();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Form f set f.active=true where f.id=:id")
    int activateById(@Param("id") Long id);
}
