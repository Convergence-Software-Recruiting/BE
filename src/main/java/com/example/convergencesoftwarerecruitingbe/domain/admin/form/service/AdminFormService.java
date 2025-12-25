package com.example.convergencesoftwarerecruitingbe.domain.admin.form.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.AdminFormCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormListItemResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminFormService {

    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public AdminFormListItemResponse create(AdminFormCreateRequest request) {
        Form form = formRepository.save(Form.create(request.getTitle(), request.getDescription()));
        return AdminFormListItemResponse.from(form);
    }

    @Transactional(readOnly = true)
    public List<AdminFormListItemResponse> list() {
        return formRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(AdminFormListItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminFormResponse getForm(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));
        List<Question> questions = questionRepository.findByFormIdOrderByOrderNoAsc(formId);
        return AdminFormResponse.of(form, questions);
    }

    @Transactional
    public void activate(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));

        formRepository.deactivateAllActive();
        formRepository.activateById(form.getId());
    }

    @Transactional
    public void deactivate() {
        formRepository.deactivateAllActive();
    }
}
