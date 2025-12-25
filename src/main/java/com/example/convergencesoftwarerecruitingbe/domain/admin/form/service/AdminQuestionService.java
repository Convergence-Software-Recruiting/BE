package com.example.convergencesoftwarerecruitingbe.domain.admin.form.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.QuestionResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminQuestionService {

    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse addQuestion(Long formId, QuestionCreateRequest req) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));
        validateModifiable(form);
        int nextOrderNo = nextOrderNo(form.getId());

        Question question = Question.builder()
                .form(form)
                .orderNo(nextOrderNo)
                .label(req.getLabel())
                .description(req.getDescription())
                .required(req.getRequired())
                .build();
        try {
            Question saved = questionRepository.save(question);
            return QuestionResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            // Simple retry in case of concurrent inserts colliding on unique (form_id, order_no)
            int retryOrderNo = nextOrderNo(form.getId());
            Question retry = Question.builder()
                    .form(form)
                    .orderNo(retryOrderNo)
                    .label(req.getLabel())
                    .description(req.getDescription())
                    .required(req.getRequired())
                    .build();
            Question saved = questionRepository.save(retry);
            return QuestionResponse.from(saved);
        }
    }

    @Transactional
    public QuestionResponse updateQuestion(Long questionId, QuestionUpdateRequest req) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        Form form = question.getForm();
        validateModifiable(form);

        // TODO: reorder endpoint 별도 구현
        question.updateContent(req.getLabel(), req.getDescription(), req.getRequired());
        return QuestionResponse.from(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        validateModifiable(question.getForm());
        questionRepository.delete(question);
    }

    private void validateModifiable(Form form) {
        if (form.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Active form cannot be modified");
        }
    }

    private int nextOrderNo(Long formId) {
        Integer maxOrderNo = questionRepository.findMaxOrderNoByFormId(formId);
        return (maxOrderNo == null ? 0 : maxOrderNo) + 1;
    }
}
