package com.crossborder.moneytransfer.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Validated credentials used to obtain a JWT access token. */
@Getter @Setter @NoArgsConstructor
public class LoginRequest {
    @NotBlank @Email @Size(max = 254) private String email;
    @NotBlank @Size(max = 72) private String password;
}
