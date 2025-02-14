package com.jj.swm.domain.study.recruitmentposition.dto.response;

import com.jj.swm.domain.study.recruitmentposition.entity.StudyRecruitmentPosition;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRecruitmentPositionResponse {

    private Long recruitmentPositionId;

    public static CreateRecruitmentPositionResponse from(StudyRecruitmentPosition recruitmentPosition) {
        return CreateRecruitmentPositionResponse.builder()
                .recruitmentPositionId(recruitmentPosition.getId())
                .build();
    }
}
