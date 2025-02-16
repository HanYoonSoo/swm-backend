package com.jj.swm.domain.study.comment.controller;

import com.jj.swm.domain.study.comment.dto.request.UpsertCommentRequest;
import com.jj.swm.domain.study.comment.dto.response.CreateCommentResponse;
import com.jj.swm.domain.study.comment.dto.response.UpdateCommentResponse;
import com.jj.swm.domain.study.comment.service.CommentCommandService;
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
@Tag(name = "StudyComment", description = "<b>[스터디 댓글]</b> API")
public class CommentCommandController {

    private final CommentCommandService commentCommandService;

    @PostMapping({"/v1/study/{studyId}/comment", "/v1/study/{studyId}/comment/{parentId}"})
    @Operation(
            summary = "스터디 댓글 생성",
            description = "스터디 댓글을 생성합니다. 대댓글도 같은 API를 사용합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201")
    public ApiResponse<CreateCommentResponse> commentAdd(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @PathVariable(value = "parentId", required = false) Long parentId,
            @Valid @RequestBody UpsertCommentRequest createRequest
    ) {
        CreateCommentResponse response = commentCommandService.addComment(
                UUID.fromString(principal.getName()),
                studyId,
                parentId,
                createRequest
        );

        return ApiResponse.created(response);
    }

    @PatchMapping("/v1/study/comment/{commentId}")
    @Operation(summary = "스터디 댓글 수정", description = "스터디 댓글을 수정합니다.")
    public ApiResponse<UpdateCommentResponse> commentModify(
            Principal principal,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody UpsertCommentRequest modifyRequest
    ) {
        UpdateCommentResponse response = commentCommandService.modifyComment(
                UUID.fromString(principal.getName()),
                commentId,
                modifyRequest
        );

        return ApiResponse.ok(response);
    }

    @DeleteMapping("/v1/study/{studyId}/comment/{commentId}")
    @Operation(summary = "스터디 댓글 삭제", description = "스터디 댓글을 삭제합니다.")
    public ApiResponse<Void> commentRemove(
            Principal principal,
            @PathVariable("studyId") Long studyId,
            @PathVariable("commentId") Long commentId
    ) {
        commentCommandService.removeComment(
                UUID.fromString(principal.getName()),
                studyId,
                commentId
        );

        return ApiResponse.ok(null);
    }
}
