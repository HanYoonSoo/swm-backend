package com.jj.swm.domain.study.recruitmentposition.controller;

import com.jj.swm.domain.study.recruitmentposition.dto.request.CreateRecruitmentPositionRequest;
import com.jj.swm.domain.study.recruitmentposition.dto.request.UpdateRecruitmentPositionRequest;
import com.jj.swm.domain.study.recruitmentposition.dto.response.CreateRecruitmentPositionResponse;
import com.jj.swm.domain.study.recruitmentposition.service.RecruitmentPositionCommandService;
import com.jj.swm.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "StudyRecruitmentPosition", description = "<b>[스터디 모집 포지션]</b> API")
public class RecruitmentPositionCommandController {

    private final RecruitmentPositionCommandService recruitmentPositionCommandService;

    @PostMapping("/v1/study/{studyId}/recruitment-position")
    @Operation(
            summary = "스터디 모집 포지션 추가",
            description = "스터디 모집 포지션을 추가합니다. 모집 포지션 개수가 10개를 초과하면 예외가 발생합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201")
    public ApiResponse<CreateRecruitmentPositionResponse> recruitmentPositionAdd(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @Valid @RequestBody CreateRecruitmentPositionRequest request
    ) {
        CreateRecruitmentPositionResponse response = recruitmentPositionCommandService.addRecruitmentPosition(
                UUID.fromString(principal.getName()),
                studyId,
                request
        );

        return ApiResponse.created(response);
    }

    @PatchMapping("/v1/study/recruitment-position/{recruitmentPositionId}")
    @Operation(summary = "스터디 모집 포지션 수정", description = "스터디 모집 포지션을 수정합니다.")
    public ApiResponse<Void> recruitmentPositionModify(
            Principal principal,
            @PathVariable("recruitmentPositionId") Long recruitmentPositionId,
            @Valid @RequestBody UpdateRecruitmentPositionRequest request
    ) {
        recruitmentPositionCommandService.modifyRecruitmentPosition(
                UUID.fromString(principal.getName()),
                recruitmentPositionId,
                request
        );

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/v1/study/recruitment-position/{recruitmentPositionId}")
    @Operation(summary = "스터디 모집 포지션 삭제", description = "스터디 모집 포지션을 삭제합니다.")
    public ApiResponse<Void> recruitmentPositionRemove(
            Principal principal, @PathVariable("recruitmentPositionId") Long recruitmentPositionId
    ) {
        recruitmentPositionCommandService.removeRecruitmentPosition(
                UUID.fromString(principal.getName()), recruitmentPositionId
        );

        return ApiResponse.ok(null);
    }

//    TODO 폼 형식일 때 재구현
//    @PostMapping("/v1/study/recruitment-position/{recruitPositionId}")
//    public ApiResponse<RecruitmentPositionApplyResponse> applyRecruitmentPosition(
//            Principal principal, @PathVariable("recruitPositionId") Long recruitPositionId
//    ) {
//        RecruitmentPositionApplyResponse applyResponse = recruitmentPositionCommandService.apply(
//                UUID.fromString("d554b429-366f-4d8e-929d-bb5479623eb9"), recruitPositionId
//        );
//
//        return ApiResponse.created(applyResponse);

//    }

//    TODO 폼 형식일 때 재 구현
//    @DeleteMapping("/v1/study/participant/{participantId}")
//    public ApiResponse<Void> cancelRecruitmentApplication(
//            Principal principal, @PathVariable("participantId") Long participantId
//    ) {
//        recruitmentPositionCommandService.withdraw(
//                UUID.fromString("d554b429-366f-4d8e-929d-bb5479623eb9"), participantId
//        );
//
//        return ApiResponse.ok(null);
//    }
}
