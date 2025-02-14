package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.comment.dto.response.FindParentCommentResponse;
import com.jj.swm.domain.study.core.entity.StudyCategory;
import com.jj.swm.domain.study.core.entity.StudyStatus;
import com.jj.swm.domain.study.recruitmentposition.dto.response.FindRecruitmentPositionResponse;
import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.global.common.dto.PageResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class FindStudyDetailsResponse {

    private Long studyId;

    private String title;

    private String content;

    private StudyCategory category;

    private int likeCount;

    private int commentCount;

    private StudyStatus status;

    private int viewCount;

    private UUID userId;

    private String nickname;

    private String profileImageUrl;

    private boolean likeStatus;

    private String openChatUrl;

    private Long studyBookmarkId;

    private List<FindStudyTagResponse> findTagResponseList;

    private List<FindStudyImageResponse> findImageResponseList;

    private List<FindRecruitmentPositionResponse> findRecruitmentPositionResponseList;

    private PageResponse<FindParentCommentResponse> pageCommentResponse;

    public static FindStudyDetailsResponse of(
            Study study,
            boolean likeStatus,
            Long studyBookmarkId,
            List<FindStudyImageResponse> findImageResponseList,
            PageResponse<FindParentCommentResponse> pageCommentResponse
    ) {
        return FindStudyDetailsResponse.builder()
                .studyId(study.getId())
                .title(study.getTitle())
                .content(study.getContent())
                .category(study.getCategory())
                .likeCount(study.getLikeCount())
                .commentCount(study.getCommentCount())
                .status(study.getStatus())
                .viewCount(study.getViewCount())
                .userId(study.getUser().getId())
                .nickname(study.getUser().getNickname())
                .profileImageUrl(study.getUser().getProfileImageUrl())
                .likeStatus(likeStatus)
                .openChatUrl(study.getOpenChatUrl())
                .studyBookmarkId(studyBookmarkId)
                .findTagResponseList(study.getStudyTagList().stream()
                        .map(FindStudyTagResponse::from)
                        .toList())
                .findImageResponseList(findImageResponseList)
                .findRecruitmentPositionResponseList(study.getStudyRecruitmentPositionList().stream()
                        .map(FindRecruitmentPositionResponse::from)
                        .toList()
                )
                .pageCommentResponse(pageCommentResponse)
                .build();
    }
}
