package com.jj.swm.domain.study.core.controller;

import com.jj.swm.domain.study.core.dto.request.AddStudyRequest;
import com.jj.swm.domain.study.core.dto.request.ModifyStudyStatusRequest;
import com.jj.swm.domain.study.core.dto.request.ModifyStudyRequest;
import com.jj.swm.domain.study.core.dto.response.AddStudyBookmarkResponse;
import com.jj.swm.domain.study.core.service.StudyCommandService;
import com.jj.swm.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudyCommandController {

    private final StudyCommandService studyCommandService;

    @PostMapping("/v1/study")
    public ApiResponse<Void> studyAdd(Principal principal, @Valid @RequestBody AddStudyRequest request) {
        studyCommandService.addStudy(UUID.fromString(principal.getName()), request);

        return ApiResponse.created(null);
    }

    @PatchMapping("/v1/study/{studyId}")
    public ApiResponse<Void> studyModify(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @Valid @RequestBody ModifyStudyRequest request
    ) {
        studyCommandService.modifyStudy(
                UUID.fromString(principal.getName()),
                studyId,
                request
        );

        return ApiResponse.ok(null);
    }

    @PatchMapping("/v1/study/{studyId}/status")
    public ApiResponse<Void> studyStatusModify(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @Valid @RequestBody ModifyStudyStatusRequest request
    ) {
        studyCommandService.modifyStudyStatus(
                UUID.fromString(principal.getName()),
                studyId,
                request
        );

        return ApiResponse.ok(null);
    }

    @DeleteMapping("/v1/study/{studyId}")
    public ApiResponse<Void> studyRemove(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.removeStudy(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.ok(null);
    }

    @PostMapping("/v1/study/{studyId}/bookmark")
    public ApiResponse<AddStudyBookmarkResponse> StudyBookmarkAdd(
            Principal principal, @PathVariable("studyId") Long studyId
    ) {
        AddStudyBookmarkResponse response = studyCommandService.addStudyBookmark(
                UUID.fromString(principal.getName()), studyId
        );

        return ApiResponse.created(response);
    }

    @DeleteMapping("/v1/study/bookmark/{bookmarkId}")
    public ApiResponse<Void> StudyBookmarkRemove(Principal principal, @PathVariable("bookmarkId") Long bookmarkId) {
        studyCommandService.removeStudyBookmark(UUID.fromString(principal.getName()), bookmarkId);

        return ApiResponse.ok(null);
    }

    @PostMapping("/v1/study/{studyId}/like")
    public ApiResponse<Void> studyLikeAdd(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.addStudyLike(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.created(null);
    }

    @DeleteMapping("/v1/study/{studyId}/like")
    public ApiResponse<Void> studyLikeRemove(Principal principal, @PathVariable("studyId") Long studyId) {
        studyCommandService.removeStudyLike(UUID.fromString(principal.getName()), studyId);

        return ApiResponse.ok(null);
    }
}
