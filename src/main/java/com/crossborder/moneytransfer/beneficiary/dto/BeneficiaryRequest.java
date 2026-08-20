package com.crossborder.moneytransfer.beneficiary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Validated write payload for creating or changing a beneficiary. */
@Getter @Setter @NoArgsConstructor
public class BeneficiaryRequest {
    @NotBlank @Size(max = 100) private String firstName;
    @NotBlank @Size(max = 100) private String lastName;
    @NotBlank @Pattern(regexp = "^[+0-9][0-9 -]{6,28}$", message = "mobile number must be valid") private String mobileNumber;
    @NotBlank @Email @Size(max = 254) private String email;
    @NotBlank @Size(max = 255) private String address;
    @NotBlank @Size(max = 100) private String city;
    @NotBlank @Size(max = 100) private String state;
    @NotBlank @Size(max = 20) private String postalCode;
    @NotBlank @Size(max = 100) private String country;
    @NotBlank @Size(max = 100) private String governmentIdNumber;
    @NotBlank @Size(max = 100) private String relationship;
}
