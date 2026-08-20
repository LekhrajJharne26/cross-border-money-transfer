package com.crossborder.moneytransfer.auth.service;

import com.crossborder.moneytransfer.auth.dto.LoginRequest;
import com.crossborder.moneytransfer.auth.dto.LoginResponse;
import com.crossborder.moneytransfer.auth.dto.RegisterRequest;

/** Coordinates registration and credential authentication use cases. */
public interface AuthenticationService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
