package com.jj.swm.domain.study.comment.dto.response;

import com.jj.swm.domain.study.comment.entity.StudyComment;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class GetCommentResponse {

    private Long commentId;

    private String content;

    private UUID userId;

    private String nickname;

    private String profileImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static GetCommentResponse from(StudyComment comment) {
        return GetCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
