package com.jj.swm.domain.study.comment.dto.response;

import com.jj.swm.domain.study.comment.entity.StudyComment;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GetParentCommentResponse extends GetCommentResponse {

    private int replyCount;

    public static GetParentCommentResponse of(StudyComment comment, int replyCount) {
        return GetParentCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replyCount(replyCount)
                .build();
    }
}
