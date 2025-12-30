package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.repository;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity.IntroPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntroPhotoRepository extends JpaRepository<IntroPhoto, Long> {

    List<IntroPhoto> findAllByOrderBySortOrderAscIdAsc();
}
