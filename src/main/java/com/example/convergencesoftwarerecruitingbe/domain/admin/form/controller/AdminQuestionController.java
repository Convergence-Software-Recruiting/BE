package com.example.convergencesoftwarerecruitingbe.domain.admin.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.QuestionResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.service.AdminQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminQuestionController implements AdminQuestionControllerDocs {

    private final AdminQuestionService adminQuestionService;

    @Override
    @PostMapping("/forms/{formId}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long formId,
            @Valid @RequestBody QuestionCreateRequest request
    ) {
        QuestionResponse response = adminQuestionService.addQuestion(formId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionUpdateRequest request
    ) {
        QuestionResponse response = adminQuestionService.updateQuestion(questionId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId
    ) {
        adminQuestionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
