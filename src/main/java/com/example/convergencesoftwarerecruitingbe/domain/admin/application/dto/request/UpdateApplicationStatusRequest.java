package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request;

import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateApplicationStatusRequest {

    @NotNull
    @Schema(
            description="변경할 상태 (RECEIVED(접수), UNDER_REVIEW(검토중), PASS(합격), FAIL(불합격_)",
            example="RECEIVED"
    )
    private ApplicationStatus status;
}
