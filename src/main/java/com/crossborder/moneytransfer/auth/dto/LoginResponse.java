package com.crossborder.moneytransfer.auth.dto;

import com.crossborder.moneytransfer.user.model.Role;
import lombok.Builder;
import lombok.Getter;

/** Authentication result returned after a successful registration or login. */
@Getter @Builder
public class LoginResponse {
    private final String accessToken;
    private final String tokenType;
    private final long expiresIn;
    private final Long userId;
    private final String email;
    private final Role role;
}
