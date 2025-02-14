package com.jj.swm.domain.user.core.service;

import com.jj.swm.domain.user.core.dto.response.GetBusinessVerificationRequestResponse;
import com.jj.swm.domain.user.core.dto.response.GetUserInfoResponse;
import com.jj.swm.domain.user.core.entity.BusinessVerificationRequest;
import com.jj.swm.domain.user.core.entity.InspectionStatus;
import com.jj.swm.domain.user.core.entity.User;
import com.jj.swm.domain.user.core.repository.BusinessVerificationRequestRepository;
import com.jj.swm.domain.user.core.repository.UserCredentialRepository;
import com.jj.swm.domain.user.core.repository.UserRepository;
import com.jj.swm.global.common.constants.PageSize;
import com.jj.swm.global.common.dto.PageResponse;
import com.jj.swm.global.common.enums.ErrorCode;
import com.jj.swm.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final BusinessVerificationRequestRepository businessVerificationRequestRepository;

    @Transactional(readOnly = true)
    public PageResponse<GetBusinessVerificationRequestResponse> getBusinessVerificationRequests(
            List<InspectionStatus> status, int pageNo
    ) {
        Pageable pageable = PageRequest.of(
                pageNo,
                PageSize.BusinessVerificationRequest,
                Sort.by("id").descending()
        );

        Page<BusinessVerificationRequest> pagedRequests
                = businessVerificationRequestRepository.findPagedVerificationRequestWithStatus(status, pageable);

        return PageResponse.of(pagedRequests, GetBusinessVerificationRequestResponse::from);
    }

    @Transactional(readOnly = true)
    public GetUserInfoResponse getUserInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_VALID, "User Not Found"));

        return GetUserInfoResponse.from(user);
    }

    public boolean validateLoginId(String loginId) {
        return userCredentialRepository.existsByLoginId(loginId);
    }

    public boolean validateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
