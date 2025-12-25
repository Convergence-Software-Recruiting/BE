package com.example.convergencesoftwarerecruitingbe.domain.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.QuestionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ActiveFormResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveFormQueryService {

    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public ActiveFormResponse getActiveForm() {
        Form activeForm = formRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "현재 모집 중인 지원서가 없습니다"));

        List<Question> questions = questionRepository.findByFormIdOrderByOrderNoAsc(activeForm.getId());
        return ActiveFormResponse.of(activeForm, questions);
    }
}
