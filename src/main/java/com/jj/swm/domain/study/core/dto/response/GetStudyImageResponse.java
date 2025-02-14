package com.jj.swm.domain.study.core.dto.response;

import com.jj.swm.domain.study.core.entity.StudyImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetStudyImageResponse {

    private Long imageId;

    private String imageUrl;

    public static GetStudyImageResponse from(StudyImage image) {
        return GetStudyImageResponse.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
