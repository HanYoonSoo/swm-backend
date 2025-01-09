package com.jj.swm.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpsertStudyRoomQnaRequest {

    @NotBlank
    private String comment;
}