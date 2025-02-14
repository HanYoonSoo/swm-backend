package com.jj.swm.domain.study.core.controller;

import com.jj.swm.domain.study.core.dto.FindStudyCondition;
import com.jj.swm.domain.study.core.dto.response.FindStudyDetailsResponse;
import com.jj.swm.domain.study.core.dto.response.FindStudyResponse;
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
    public ApiResponse<PageResponse<FindStudyResponse>> studyList(Principal principal, FindStudyCondition condition) {
        PageResponse<FindStudyResponse> pageResponse = studyQueryService.findStudyList(
                principal != null ? UUID.fromString(principal.getName()) : null, condition
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/{studyId}")
    public ApiResponse<FindStudyDetailsResponse> studyDetails(
            Principal principal, @PathVariable("studyId") Long studyId
    ) {
        FindStudyDetailsResponse response = studyQueryService.findStudy(
                principal != null ? UUID.fromString(principal.getName()) : null, studyId
        );

        return ApiResponse.ok(response);
    }

    @GetMapping("/v1/study/user/liked-studies")
    public ApiResponse<PageResponse<FindStudyResponse>> userLikedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<FindStudyResponse> pageResponse = studyQueryService.findUserLikedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/user/bookmarked-studies")
    public ApiResponse<PageResponse<FindStudyResponse>> userBookmarkedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<FindStudyResponse> pageResponse = studyQueryService.findUserBookmarkedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }
}
