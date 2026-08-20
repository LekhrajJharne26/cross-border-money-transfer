package com.crossborder.moneytransfer.auth.service.impl;

import com.crossborder.moneytransfer.auth.dto.LoginRequest;
import com.crossborder.moneytransfer.auth.dto.LoginResponse;
import com.crossborder.moneytransfer.auth.dto.RegisterRequest;
import com.crossborder.moneytransfer.auth.service.AuthenticationService;
import com.crossborder.moneytransfer.config.JwtProperties;
import com.crossborder.moneytransfer.exception.DuplicateResourceException;
import com.crossborder.moneytransfer.exception.InvalidCredentialsException;
import com.crossborder.moneytransfer.exception.ResourceNotFoundException;
import com.crossborder.moneytransfer.security.JwtService;
import com.crossborder.moneytransfer.user.entity.User;
import com.crossborder.moneytransfer.user.model.Role;
import com.crossborder.moneytransfer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

/** Default implementation that hashes passwords, verifies credentials, and issues tokens. */
@Service @RequiredArgsConstructor @Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    public LoginResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userService.existsByEmail(email)) throw new DuplicateResourceException("Email address is already registered");
        User user = userService.create(User.builder().firstName(request.getFirstName().trim()).lastName(request.getLastName().trim())
                .email(email).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build());
        return tokenResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user;
        try { user = userService.findByEmail(normalizeEmail(request.getEmail())); }
        catch (ResourceNotFoundException ignored) { throw new InvalidCredentialsException(); }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new InvalidCredentialsException();
        return tokenResponse(user);
    }

    private LoginResponse tokenResponse(User user) {
        String token = jwtService.generateToken(user.getUsername(), Map.of("role", user.getRole().name()));
        return LoginResponse.builder().accessToken(token).tokenType("Bearer").expiresIn(jwtProperties.getExpirationMs() / 1000)
                .userId(user.getId()).email(user.getEmail()).role(user.getRole()).build();
    }

    private String normalizeEmail(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}
