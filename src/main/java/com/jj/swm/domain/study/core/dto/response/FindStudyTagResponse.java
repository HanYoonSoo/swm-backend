package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.StudyTag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindStudyTagResponse {

    private Long tagId;

    private String name;

    public static FindStudyTagResponse from(StudyTag studyTag) {
        return FindStudyTagResponse.builder()
                .tagId(studyTag.getId())
                .name(studyTag.getName())
                .build();
    }
}
