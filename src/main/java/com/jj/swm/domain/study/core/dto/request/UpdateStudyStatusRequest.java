package com.jj.swm.domain.study.core.dto.request;

import com.jj.swm.domain.study.core.entity.StudyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStudyStatusRequest {

    @NotNull
    private StudyStatus status;
}
