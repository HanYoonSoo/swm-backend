package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.StudyBookmark;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateStudyBookmarkResponse {

    private Long bookmarkId;

    public static CreateStudyBookmarkResponse from(StudyBookmark studyBookmark) {
        return CreateStudyBookmarkResponse.builder()
                .bookmarkId(studyBookmark.getId())
                .build();
    }
}
