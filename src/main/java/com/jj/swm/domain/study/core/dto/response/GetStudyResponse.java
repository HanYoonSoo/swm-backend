package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.entity.StudyBookmark;
import com.jj.swm.domain.study.core.entity.StudyCategory;
import com.jj.swm.domain.study.core.entity.StudyStatus;
import com.jj.swm.domain.study.recruitmentposition.dto.response.GetRecruitmentPositionResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetStudyResponse {

    private Long studyId;

    private String title;

    private String content;

    private StudyCategory category;

    private int likeCount;

    private int commentCount;

    private StudyStatus status;

    private int viewCount;

    private Long studyBookmarkId;

    private List<GetStudyTagResponse> getTagResponseList;

    private List<GetRecruitmentPositionResponse> getRecruitmentPositionResponseList;

    public static GetStudyResponse of(Study study, Long studyBookmarkId) {
        return GetStudyResponse.builder()
                .studyId(study.getId())
                .title(study.getTitle())
                .content(study.getContent())
                .category(study.getCategory())
                .likeCount(study.getLikeCount())
                .commentCount(study.getCommentCount())
                .status(study.getStatus())
                .viewCount(study.getViewCount())
                .studyBookmarkId(studyBookmarkId)
                .getTagResponseList(study.getStudyTagList().stream()
                        .map(GetStudyTagResponse::from)
                        .toList())
                .getRecruitmentPositionResponseList(study.getStudyRecruitmentPositionList().stream()
                        .map(GetRecruitmentPositionResponse::from)
                        .toList())
                .build();
    }

    public static GetStudyResponse of(StudyBookmark studyBookmark) {
        return GetStudyResponse.builder()
                .studyId(studyBookmark.getStudy().getId())
                .title(studyBookmark.getStudy().getTitle())
                .content(studyBookmark.getStudy().getContent())
                .category(studyBookmark.getStudy().getCategory())
                .likeCount(studyBookmark.getStudy().getLikeCount())
                .commentCount(studyBookmark.getStudy().getCommentCount())
                .status(studyBookmark.getStudy().getStatus())
                .viewCount(studyBookmark.getStudy().getViewCount())
                .studyBookmarkId(studyBookmark.getId())
                .getTagResponseList(studyBookmark.getStudy().getStudyTagList().stream()
                        .map(GetStudyTagResponse::from)
                        .toList())
                .getRecruitmentPositionResponseList(studyBookmark.getStudy().getStudyRecruitmentPositionList().stream()
                        .map(GetRecruitmentPositionResponse::from)
                        .toList())
                .build();
    }
}
