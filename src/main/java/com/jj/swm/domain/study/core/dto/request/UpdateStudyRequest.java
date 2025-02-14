package com.jj.swm.domain.study.core.dto.request;

import com.jj.swm.domain.study.core.entity.StudyCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStudyRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String openChatUrl;

    @NotNull
    private StudyCategory category;

    @Valid
    private SaveStudyTagRequest saveTagRequest;

    @Valid
    private SaveStudyImageRequest saveImageRequest;
}
