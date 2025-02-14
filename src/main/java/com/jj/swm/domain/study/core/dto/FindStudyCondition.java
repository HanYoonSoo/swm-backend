package com.jj.swm.domain.study.core.dto;

import com.jj.swm.domain.study.core.entity.StudyCategory;
import com.jj.swm.domain.study.core.entity.StudyStatus;
import com.jj.swm.domain.study.recruitmentposition.entity.RecruitmentPositionTitle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FindStudyCondition {

    private String title;

    private StudyCategory category;

    private StudyStatus status;

    private List<RecruitmentPositionTitle> recruitmentPositionTitleList = new ArrayList<>();

    private Long lastStudyId;

    private SortCriteria sortCriteria = SortCriteria.NEWEST;

    private Integer lastSortValue;
}
