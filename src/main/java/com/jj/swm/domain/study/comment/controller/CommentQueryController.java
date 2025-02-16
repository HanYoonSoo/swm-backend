package com.jj.swm.domain.study.comment.controller;

import com.jj.swm.domain.study.comment.dto.response.GetParentCommentResponse;
import com.jj.swm.domain.study.comment.dto.response.GetCommentResponse;
import com.jj.swm.domain.study.comment.service.CommentQueryService;
import com.jj.swm.global.common.dto.ApiResponse;
import com.jj.swm.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "StudyComment", description = "<b>[스터디 댓글]</b> API")
public class CommentQueryController {

    private final CommentQueryService commentQueryService;

    @GetMapping("/v1/study/{studyId}/comment")
    public ApiResponse<PageResponse<GetParentCommentResponse>> commentList(
            @PathVariable("studyId") Long studyId, @RequestParam("pageNo") int pageNo
    ) {
        PageResponse<GetParentCommentResponse> pageResponse = commentQueryService.findCommentList(studyId, pageNo);

        return ApiResponse.ok(pageResponse);
    }

    @GetMapping({"/v1/comment/{parentId}/reply", "/v1/comment/{parentId}/reply/{lastReplyId}"})
    public ApiResponse<PageResponse<GetCommentResponse>> replyList(
            @PathVariable("parentId") Long parentId,
            @PathVariable(value = "lastReplyId", required = false) Long lastReplyId
    ) {
        PageResponse<GetCommentResponse> pageResponse = commentQueryService.findReplyList(parentId, lastReplyId);

        return ApiResponse.ok(pageResponse);
    }
}
