package com.crossborder.moneytransfer.auth.controller;

import com.crossborder.moneytransfer.auth.dto.LoginRequest;
import com.crossborder.moneytransfer.auth.dto.LoginResponse;
import com.crossborder.moneytransfer.auth.dto.RegisterRequest;
import com.crossborder.moneytransfer.auth.service.AuthenticationService;
import com.crossborder.moneytransfer.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes the public endpoints used to register and authenticate users. */
@RestController @RequestMapping("/v1/auth") @RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and JWT login")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a user", description = "Creates a USER account and returns a JWT access token.")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully", authenticationService.register(request)));
    }

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Validates email and password, then returns a JWT access token.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", authenticationService.login(request)));
    }
}
