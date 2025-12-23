package com.example.convergencesoftwarerecruitingbe.domain.form.service;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersionStatus;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.QuestionType;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.FormVersionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class FormLocalSeeder {

    private final FormRepository formRepository;
    private final FormVersionRepository formVersionRepository;
    private final QuestionRepository questionRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        String title = "비대위 모집";
        if (formRepository.findByTitle(title).isPresent()) {
            log.info("Form seed skipped: '{}' already exists", title);
            return;
        }

        Form form = formRepository.save(Form.builder()
                .title(title)
                .description(null)
                .active(true)
                .build());

        FormVersion version = formVersionRepository.save(FormVersion.builder()
                .form(form)
                .version(1)
                .status(FormVersionStatus.PUBLISHED)
                .publishedAt(LocalDateTime.now())
                .build());

        List<Question> questions = List.of(
                Question.builder()
                        .formVersion(version)
                        .orderNo(1)
                        .label("지원 동기를 작성해주세요")
                        .description(null)
                        .type(QuestionType.TEXTAREA)
                        .required(true)
                        .build(),
                Question.builder()
                        .formVersion(version)
                        .orderNo(2)
                        .label("자기소개를 작성해주세요")
                        .description(null)
                        .type(QuestionType.TEXTAREA)
                        .required(true)
                        .build(),
                Question.builder()
                        .formVersion(version)
                        .orderNo(3)
                        .label("하고 싶은 역할/파트가 있다면 작성해주세요")
                        .description(null)
                        .type(QuestionType.TEXTAREA)
                        .required(false)
                        .build()
        );
        questionRepository.saveAll(questions);
        log.info("Seeded sample form '{}' with {} questions", title, questions.size());
    }
}
