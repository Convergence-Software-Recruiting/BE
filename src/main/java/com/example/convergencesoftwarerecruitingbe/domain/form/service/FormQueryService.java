package com.example.convergencesoftwarerecruitingbe.domain.form.service;

import com.example.convergencesoftwarerecruitingbe.domain.form.dto.FormActiveResponse;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersionStatus;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.FormVersionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormQueryService {

    private final FormVersionRepository formVersionRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public FormActiveResponse getActiveForm() {
        FormVersion version = formVersionRepository.findTopByStatusOrderByPublishedAtDesc(FormVersionStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active form not found"));

        Form form = version.getForm(); // 여기서 proxy라도 OK (세션 살아있음)
        if (form == null || !form.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Active form not found");
        }

        List<Question> questions = questionRepository.findByFormVersionIdOrderByOrderNoAsc(version.getId());
        return FormActiveResponse.of(form, version, questions);
    }
}
