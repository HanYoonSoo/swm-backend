package com.jj.swm.domain.auth.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String loginId;
    private String password;
}
