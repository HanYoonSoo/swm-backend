package com.jj.swm.domain.user.core.controller;

import com.jj.swm.domain.user.core.dto.response.GetBusinessVerificationRequestResponse;
import com.jj.swm.domain.user.core.dto.response.GetUserInfoResponse;
import com.jj.swm.domain.user.core.entity.InspectionStatus;
import com.jj.swm.domain.user.core.service.UserQueryService;
import com.jj.swm.global.common.dto.ApiResponse;
import com.jj.swm.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "<b>[유저]</b> API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserQueryController {

    private final UserQueryService userQueryService;

    @Operation(
            summary = "유저 이메일 중복 검사",
            description = "유저 이메일 중복 여부를 검사합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "성공"
    )
    @GetMapping("/v1/user/login-id/validation")
    public ApiResponse<Boolean> validateUserLoginId(@Email @NotBlank @RequestParam("loginId") String loginId) {
        boolean response = userQueryService.validateLoginId(loginId);

        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "유저 닉네임 중복 검사",
            description = "유저 닉네임 중복 여부를 검사합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "성공"
    )
    @GetMapping("/v1/user/nickname/validation")
    public ApiResponse<Boolean> validateUserNickname(@RequestParam("nickname") String nickname) {
        boolean response = userQueryService.validateNickname(nickname);

        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "유저 정보 반환",
            description = "유저의 정보를 읽습니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "성공"
    )
    @GetMapping("/v1/user")
    public ApiResponse<GetUserInfoResponse> getUserInfo(Principal principal){
        GetUserInfoResponse response
                = userQueryService.getUserInfo(UUID.fromString(principal.getName()));

        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "유저 사업자 검수 요청 목록 조회",
            description = "유저 사업자 검수 요청 조회합니다." +
                    "status(기본값): PENDING, APPROVED, REJECTED, " +
                    "pageNo(기본값): 0"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "성공"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/v1/user/business/verification/requests")
    public ApiResponse<PageResponse<GetBusinessVerificationRequestResponse>> getBusinessVerificationRequests(
            @RequestParam(
                    value = "pageNo",
                    required = false,
                    defaultValue = "0"
            ) int pageNo,
            @RequestParam(
                    value = "status",
                    required = false,
                    defaultValue = "PENDING, APPROVED, REJECTED"
            ) List<InspectionStatus> status
    ) {
        PageResponse<GetBusinessVerificationRequestResponse> response
                = userQueryService.getBusinessVerificationRequests(status, pageNo);

        return ApiResponse.ok(response);
    }

    @Operation(
            summary = "헬스 체크",
            description = "헬스 체크."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "성공"
    )
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("OK");
    }
}
