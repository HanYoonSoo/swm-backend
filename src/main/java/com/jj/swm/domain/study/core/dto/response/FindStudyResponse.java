package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.entity.StudyBookmark;
import com.jj.swm.domain.study.core.entity.StudyCategory;
import com.jj.swm.domain.study.core.entity.StudyStatus;
import com.jj.swm.domain.study.recruitmentposition.dto.response.FindRecruitmentPositionResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindStudyResponse {

    private Long studyId;

    private String title;

    private String content;

    private StudyCategory category;

    private int likeCount;

    private int commentCount;

    private StudyStatus status;

    private int viewCount;

    private Long studyBookmarkId;

    private List<FindStudyTagResponse> findTagResponseList;

    private List<FindRecruitmentPositionResponse> findRecruitmentPositionResponseList;

    public static FindStudyResponse of(Study study, Long studyBookmarkId) {
        return FindStudyResponse.builder()
                .studyId(study.getId())
                .title(study.getTitle())
                .content(study.getContent())
                .category(study.getCategory())
                .likeCount(study.getLikeCount())
                .commentCount(study.getCommentCount())
                .status(study.getStatus())
                .viewCount(study.getViewCount())
                .studyBookmarkId(studyBookmarkId)
                .findTagResponseList(study.getStudyTagList().stream()
                        .map(FindStudyTagResponse::from)
                        .toList())
                .findRecruitmentPositionResponseList(study.getStudyRecruitmentPositionList().stream()
                        .map(FindRecruitmentPositionResponse::from)
                        .toList())
                .build();
    }

    public static FindStudyResponse of(StudyBookmark studyBookmark) {
        return FindStudyResponse.builder()
                .studyId(studyBookmark.getStudy().getId())
                .title(studyBookmark.getStudy().getTitle())
                .content(studyBookmark.getStudy().getContent())
                .category(studyBookmark.getStudy().getCategory())
                .likeCount(studyBookmark.getStudy().getLikeCount())
                .commentCount(studyBookmark.getStudy().getCommentCount())
                .status(studyBookmark.getStudy().getStatus())
                .viewCount(studyBookmark.getStudy().getViewCount())
                .studyBookmarkId(studyBookmark.getId())
                .findTagResponseList(studyBookmark.getStudy().getStudyTagList().stream()
                        .map(FindStudyTagResponse::from)
                        .toList())
                .findRecruitmentPositionResponseList(studyBookmark.getStudy().getStudyRecruitmentPositionList().stream()
                        .map(FindRecruitmentPositionResponse::from)
                        .toList())
                .build();
    }
}
