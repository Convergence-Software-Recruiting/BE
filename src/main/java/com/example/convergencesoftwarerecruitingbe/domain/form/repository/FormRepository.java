package com.example.convergencesoftwarerecruitingbe.domain.form.repository;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {
    Optional<Form> findByTitle(String title);
}
