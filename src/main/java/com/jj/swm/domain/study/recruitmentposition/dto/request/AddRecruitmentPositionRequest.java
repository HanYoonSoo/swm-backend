package com.jj.swm.domain.study.recruitmentposition.dto.request;

import com.jj.swm.domain.study.recruitmentposition.entity.RecruitmentPositionTitle;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddRecruitmentPositionRequest {

    @NotNull
    private RecruitmentPositionTitle title;

    @Min(1)
    @Max(100)
    @NotNull
    private Integer headcount;
}
