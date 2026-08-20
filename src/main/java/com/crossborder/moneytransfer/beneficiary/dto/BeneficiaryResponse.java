package com.crossborder.moneytransfer.beneficiary.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

/** Read model returned to the beneficiary owner after an authorized operation. */
@Getter @Builder
public class BeneficiaryResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String mobileNumber;
    private final String email;
    private final String address;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;
    private final String governmentIdNumber;
    private final String relationship;
    private final Instant createdAt;
    private final Instant updatedAt;
}
