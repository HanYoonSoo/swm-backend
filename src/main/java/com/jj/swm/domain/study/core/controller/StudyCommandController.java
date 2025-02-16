package com.jj.swm.domain.study.core.controller;

import com.jj.swm.domain.study.core.dto.request.CreateStudyRequest;
import com.jj.swm.domain.study.core.dto.request.UpdateStudyRequest;
import com.jj.swm.domain.study.core.dto.request.UpdateStudyStatusRequest;
import com.jj.swm.domain.study.core.dto.response.CreateStudyBookmarkResponse;
import com.jj.swm.domain.study.core.service.StudyCommandService;
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
@Tag(name = "Study", description = "<b>[스터디]</b> API")
public class StudyCommandController {

    private final StudyCommandService studyCommandService;

    @PostMapping("/v1/study")
    @Operation(summary = "스터디 생성", description = "로그인한 유저가 스터디를 생성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201")
    public ApiResponse<Void> studyAdd(Principal principal, @Valid @RequestBody CreateStudyRequest request) {
        studyCommandService.addStudy(UUID.fromString(principal.getName()), request);

        return ApiResponse.created(null);
    }

    @PatchMapping("/v1/study/{studyId}")
    @Operation(
            summary = "스터디 수정",
            description = """
                    로그인한, 해당 글을 작성한 유저가 스터디를 수정합니다.
                    리스트 추가/삭제를 제외한 기존 데이터는 넣어주셔야 합니다.<br>
                    태그, 이미지의 최대 개수는 10개입니다.
                    """
    )
    public ApiResponse<Void> studyModify(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @Valid @RequestBody UpdateStudyRequest request
    ) {
        studyCommandService.modifyStudy(
                UUID.fromString(principal.getName()),
                studyId,
                request
        );

        return ApiResponse.ok(null);
    }

    @PatchMapping("/v1/study/{studyId}/status")
    @Operation(
            summary = "스터디 상태 수정",
            description = """
                    로그인한, 해당 글을 작성한 유저가 스터디 상태를 수정합니다.
                    여기서 스터디 상태는 모집중인지에 대한 것입니다.<br>
                    태그, 이미지, 모집 포지션의 최대 개수는 10개입니다.
                    """
    )
    public ApiResponse<Void> studyStatusModify(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @Valid @RequestBody UpdateStudyStatusRequest request
    ) {
        studyCommandService.modifyStudyStatus(
                UUID.fromString(principal.getName()),
                studyId,
                request
        );

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/v1/study/{studyId}")
    @Operation(
            summary = "스터디 삭제",
            description = "로그인한, 해당 글을 작성한 유저가 스터디를 삭제합니다. 관련된 모든 정보가 삭제됩니다."
    )
    public ApiResponse<Void> studyRemove(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.removeStudy(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.ok(null);
    }

    @PostMapping("/v1/study/{studyId}/bookmark")
    @Operation(
            summary = "스터디 북마크",
            description = "스터디 북마크 등록입니다. 유저마다 북마크는 한 번만 등록할 수 있습니다.")

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201")
    public ApiResponse<CreateStudyBookmarkResponse> StudyBookmarkAdd(
            Principal principal, @PathVariable("studyId") Long studyId
    ) {
        CreateStudyBookmarkResponse response = studyCommandService.addStudyBookmark(
                UUID.fromString(principal.getName()), studyId
        );

        return ApiResponse.created(response);
    }

    @DeleteMapping("/v1/study/bookmark/{bookmarkId}")
    @Operation(summary = "스터디 북마크 취소", description = "스터디 북마크 취소입니다.")
    public ApiResponse<Void> StudyBookmarkRemove(Principal principal, @PathVariable("bookmarkId") Long bookmarkId) {
        studyCommandService.removeStudyBookmark(UUID.fromString(principal.getName()), bookmarkId);

        return ApiResponse.ok(null);
    }

    @PostMapping("/v1/study/{studyId}/like")
    @Operation(
            summary = "스터디 좋아요",
            description = "스터디 좋아요 등록입니다. 유저마다 좋아요는 한 번만 누를 수 있습니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201")
    public ApiResponse<Void> studyLikeAdd(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.addStudyLike(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.created(null);
    }

    @DeleteMapping("/v1/study/{studyId}/like")
    @Operation(summary = "스터디 좋아요 취소", description = "스터디 좋아요 취소입니다.")
    public ApiResponse<Void> studyLikeRemove(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.removeStudyLike(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.ok(null);
    }
}
