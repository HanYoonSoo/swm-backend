package com.jj.swm.domain.study.core.controller;

import com.jj.swm.domain.study.core.dto.GetStudyCondition;
import com.jj.swm.domain.study.core.dto.response.GetStudyDetailsResponse;
import com.jj.swm.domain.study.core.dto.response.GetStudyResponse;
import com.jj.swm.domain.study.core.service.StudyQueryService;
import com.jj.swm.global.common.dto.ApiResponse;
import com.jj.swm.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Study", description = "<b>[스터디]</b> API")
public class StudyQueryController {

    private final StudyQueryService studyQueryService;

    @GetMapping("/v1/study")
    @Operation(
            summary = "스터디 목록 조회",
            description = """
                     무한 스크롤 방식으로 스터디 목록을 조회합니다.<br>
                     제목, 카테고리, 모집 상태로 필터링 할 수 있습니다.<br><br>
                     <b>[정렬 별 활용 필드 - lastStudyId 필수]</b><br>
                     STAR: req -> lastAverageRatingValue, res -> starAvg<br>
                     (LIKE, NEWEST, COMMENT): req -> lastSortValue,
                     res -> likeCount, commentCount
                    """
    )
    public ApiResponse<PageResponse<GetStudyResponse>> studyList(Principal principal, GetStudyCondition condition) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findStudyList(
                principal != null ? UUID.fromString(principal.getName()) : null, condition
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/{studyId}")
    @Operation(summary = "스터디 상세 조회", description = "스터디를 상세 조회합니다.")
    public ApiResponse<GetStudyDetailsResponse> studyDetails(
            Principal principal, @PathVariable("studyId") Long studyId
    ) {
        GetStudyDetailsResponse response = studyQueryService.findStudy(
                principal != null ? UUID.fromString(principal.getName()) : null, studyId
        );

        return ApiResponse.ok(response);
    }

    @GetMapping("/v1/study/user/liked-studies")
    @Operation(
            summary = "특정 유저가 좋아요한 스터디 목록 조회",
            description = "특정 유저가 좋아요한 스터디를 페이징 조회합니다.<br>" +
                    "pageNo는 필수값입니다. 가장 첫 페이지는 pageNo가 0입니다."
    )
    public ApiResponse<PageResponse<GetStudyResponse>> userLikedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findUserLikedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/v1/study/user/bookmarked-studies")
    @Operation(
            summary = "특정 유저가 북마크한 스터디 목록 조회",
            description = "특정 유저가 북마크한 스터디를 페이징 조회합니다.<br>" +
                    "pageNo는 필수값입니다. 가장 첫 페이지는 pageNo가 0입니다."
    )
    public ApiResponse<PageResponse<GetStudyResponse>> userBookmarkedStudyList(
            Principal principal, @RequestParam(value = "pageNo") int pageNo
    ) {
        PageResponse<GetStudyResponse> pageResponse = studyQueryService.findUserBookmarkedStudyList(
                UUID.fromString(principal.getName()), pageNo
        );

        return ApiResponse.ok(pageResponse);
    }
}
