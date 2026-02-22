package com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.form.service;

import com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.form.repository.QuestionRepository;
import com.example.convergencesoftwarerecruitingbe.domain.recruit.client.application.repository.ApplicationAnswerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.recruit.client.application.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminFormServiceDeleteTest {

    @Mock
    FormRepository formRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    ApplicationAnswerRepository applicationAnswerRepository;

    @InjectMocks
    AdminFormService adminFormService;

    @Test
    @DisplayName("활성 폼 삭제 시 자동 비활성화 후 답변/지원서/질문/폼 순서로 삭제한다")
    void deleteForm_activeForm_deletesAllRelatedData() {
        // given
        Long formId = 10L;
        Form form = Form.builder()
                .id(formId)
                .title("2026 상반기 모집")
                .description("desc")
                .active(true)
                .resultOpen(true)
                .build();

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));

        // when
        adminFormService.deleteForm(formId);

        // then
        assertThat(form.isActive()).isFalse();
        var inOrder = inOrder(applicationAnswerRepository, applicationRepository, questionRepository, formRepository);
        inOrder.verify(applicationAnswerRepository).deleteByApplicationFormId(formId);
        inOrder.verify(applicationRepository).deleteByFormId(formId);
        inOrder.verify(questionRepository).deleteByFormId(formId);
        inOrder.verify(formRepository).delete(form);
    }

    @Test
    @DisplayName("삭제 대상 폼이 없으면 404를 반환한다")
    void deleteForm_notFound_throws404() {
        // given
        Long formId = 404L;
        when(formRepository.findById(formId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminFormService.deleteForm(formId))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });

        verify(formRepository).findById(formId);
        verifyNoInteractions(applicationAnswerRepository, applicationRepository, questionRepository);
    }
}
