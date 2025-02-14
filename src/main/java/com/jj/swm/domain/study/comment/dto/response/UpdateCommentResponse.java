package com.jj.swm.domain.study.comment.dto.response;

import com.jj.swm.domain.study.comment.entity.StudyComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateCommentResponse {

    private LocalDateTime updatedAt;

    public static UpdateCommentResponse from(StudyComment comment) {
        return UpdateCommentResponse.builder()
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
