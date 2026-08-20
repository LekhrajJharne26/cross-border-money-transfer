package com.crossborder.moneytransfer.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Validated client payload for creating a normal platform user. */
@Getter @Setter @NoArgsConstructor
public class RegisterRequest {
    @NotBlank @Size(max = 100) private String firstName;
    @NotBlank @Size(max = 100) private String lastName;
    @NotBlank @Email @Size(max = 254) private String email;
    @NotBlank @Size(min = 8, max = 72)
    @Pattern(regexp = ".*\\d.*", message = "password must contain at least one number")
    private String password;
}
