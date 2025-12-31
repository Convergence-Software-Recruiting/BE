package com.example.convergencesoftwarerecruitingbe.domain.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.QuestionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationSubmitResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationAnswerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
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

    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationAnswerRepository applicationAnswerRepository;
    private final ApplicationResultCodeGenerator applicationResultCodeGenerator;

    @Transactional
    public ApplicationSubmitResponse submitToActiveForm(ApplicationSubmitRequest request) {
        Form form = formRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "현재 모집 중인 지원서가 없습니다"));
        if (!form.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "활성화되지 않은 모집입니다");
        }

        if (applicationRepository.existsByFormIdAndStudentNo(form.getId(), request.getStudentNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 제출된 학번입니다");
        }

        List<Question> questions = questionRepository.findByFormIdOrderByOrderNoAsc(form.getId());
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 질문입니다");
            }
            if (answerMap.containsKey(questionId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변에 중복된 질문이 있습니다");
            }
            answerMap.put(questionId, answerItem.getValue());
        }

        for (Long requiredId : requiredQuestionIds) {
            String value = answerMap.get(requiredId);
            if (!StringUtils.hasText(value)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 질문의 답변이 필요합니다");
            }
        }

        Application application = Application.builder()
                .form(form)
                .name(request.getName())
                .studentNo(request.getStudentNo())
                .major(request.getMajor())
                .grade(request.getGrade())
                .phone(request.getPhone())
                .resultCode(applicationResultCodeGenerator.generate(form.getId()))
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

        return new ApplicationSubmitResponse(application.getId(), application.getResultCode());
    }
}
