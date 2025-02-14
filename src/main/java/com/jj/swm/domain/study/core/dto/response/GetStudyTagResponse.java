package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.StudyTag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetStudyTagResponse {

    private Long tagId;

    private String name;

    public static GetStudyTagResponse from(StudyTag studyTag) {
        return GetStudyTagResponse.builder()
                .tagId(studyTag.getId())
                .name(studyTag.getName())
                .build();
    }
}
