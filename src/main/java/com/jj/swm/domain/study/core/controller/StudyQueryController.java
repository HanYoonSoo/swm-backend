package com.jj.swm.domain.study.core.controller;

import com.jj.swm.domain.study.core.dto.GetStudyCondition;
import com.jj.swm.domain.study.core.dto.response.GetStudyDetailsResponse;
import com.jj.swm.domain.study.core.dto.response.GetStudyResponse;
import com.jj.swm.domain.study.core.service.StudyQueryService;
import com.jj.swm.global.common.dto.ApiResponse;
import com.jj.swm.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudyQueryController {

    private final StudyQueryService studyQueryService;

    @GetMapping("/v1/study")
    public ApiResponse<PageResponse<GetStudyResponse>> studyList(Principal principal, GetStudyCondition condition) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findStudyList(
                principal != null ? UUID.fromString(principal.getName()) : null, condition
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/{studyId}")
    public ApiResponse<GetStudyDetailsResponse> studyDetails(
            Principal principal, @PathVariable("studyId") Long studyId
    ) {
        GetStudyDetailsResponse response = studyQueryService.findStudy(
                principal != null ? UUID.fromString(principal.getName()) : null, studyId
        );

        return ApiResponse.ok(response);
    }

    @GetMapping("/v1/study/user/liked-studies")
    public ApiResponse<PageResponse<GetStudyResponse>> userLikedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findUserLikedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/user/bookmarked-studies")
    public ApiResponse<PageResponse<GetStudyResponse>> userBookmarkedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findUserBookmarkedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }
}
