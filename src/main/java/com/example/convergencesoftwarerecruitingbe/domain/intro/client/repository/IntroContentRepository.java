package com.example.convergencesoftwarerecruitingbe.domain.intro.client.repository;

import com.example.convergencesoftwarerecruitingbe.domain.intro.client.entity.IntroContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntroContentRepository extends JpaRepository<IntroContent, Long> {

    Optional<IntroContent> findTopByOrderByIdAsc();
}
