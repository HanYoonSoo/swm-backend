package com.jj.swm.domain.study.recruitmentposition.dto.response;

import com.jj.swm.domain.study.recruitmentposition.entity.RecruitmentPositionTitle;
import com.jj.swm.domain.study.recruitmentposition.entity.StudyRecruitmentPosition;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetRecruitmentPositionResponse {

    private Long recruitmentPositionId;

    private RecruitmentPositionTitle title;

    private Integer headcount;

    private Integer acceptedCount;

    public static GetRecruitmentPositionResponse from(StudyRecruitmentPosition recruitmentPosition) {
        return GetRecruitmentPositionResponse.builder()
                .recruitmentPositionId(recruitmentPosition.getId())
                .title(recruitmentPosition.getTitle())
                .headcount(recruitmentPosition.getHeadcount())
                .acceptedCount(recruitmentPosition.getAcceptedCount())
                .build();
    }
}
