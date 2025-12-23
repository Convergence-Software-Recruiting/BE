package com.example.convergencesoftwarerecruitingbe.domain.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationAnswerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersionStatus;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.FormVersionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.form.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicationSubmitService {

    private final FormVersionRepository formVersionRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationAnswerRepository applicationAnswerRepository;

    @Transactional
    public ApplicationSubmitResponse submit(Long formId, ApplicationSubmitRequest request) {
        FormVersion version = formVersionRepository.findTopByFormIdAndStatusOrderByVersionDesc(formId, FormVersionStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form version not found"));

        if (applicationRepository.existsByFormVersionIdAndStudentNo(version.getId(), request.getStudentNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student number already submitted for this form version");
        }

        List<Question> questions = questionRepository.findByFormVersionIdOrderByOrderNoAsc(version.getId());
        Map<Long, Question> questionMap = new HashMap<>();
        Set<Long> requiredQuestionIds = new HashSet<>();
        for (Question question : questions) {
            questionMap.put(question.getId(), question);
            if (question.isRequired()) {
                requiredQuestionIds.add(question.getId());
            }
        }

        Map<Long, String> answerMap = new HashMap<>();
        for (ApplicationSubmitRequest.AnswerItem answerItem : request.getAnswers()) {
            Long questionId = answerItem.getQuestionId();
            if (!questionMap.containsKey(questionId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question in answers");
            }
            answerMap.put(questionId, answerItem.getValue());
        }

        for (Long requiredId : requiredQuestionIds) {
            String value = answerMap.get(requiredId);
            if (!StringUtils.hasText(value)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required question missing answer");
            }
        }

        Application application = Application.builder()
                .formVersion(version)
                .name(request.getName())
                .studentNo(request.getStudentNo())
                .major(request.getMajor())
                .grade(request.getGrade())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
        applicationRepository.save(application);

        List<ApplicationAnswer> answers = answerMap.entrySet().stream()
                .map(entry -> ApplicationAnswer.builder()
                        .application(application)
                        .question(questionMap.get(entry.getKey()))
                        .value(entry.getValue())
                        .build())
                .toList();
        applicationAnswerRepository.saveAll(answers);

        return new ApplicationSubmitResponse(application.getId());
    }
}
