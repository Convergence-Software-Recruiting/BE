package com.example.convergencesoftwarerecruitingbe.domain.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository.FormRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationResultQueryRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationResultResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ApplicationResultQueryService {

    private final FormRepository formRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public ApplicationResultResponse query(ApplicationResultQueryRequest request) {
        Form form = formRepository.findFirstByResultOpenTrueOrderByIdDesc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "결과 조회가 아직 열리지 않았습니다"));

        String resultCode = request.getResultCode().trim().toUpperCase();
        Application application = applicationRepository.findByFormIdAndResultCode(form.getId(), resultCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "결과 코드를 확인해주세요"));

        if (application.getStatus() == ApplicationStatus.PASS) {
            return new ApplicationResultResponse("PASS");
        }
        if (application.getStatus() == ApplicationStatus.FAIL) {
            return new ApplicationResultResponse("FAIL");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "결과가 아직 확정되지 않았습니다");
    }
}
