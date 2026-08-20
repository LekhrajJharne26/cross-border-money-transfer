package com.crossborder.moneytransfer.partner.dto;

import lombok.Builder;
import lombok.Getter;

/** Read-only active banking partner representation exposed by the catalogue API. */
@Getter @Builder
public class PartnerResponse {
    private final Long id;
    private final String partnerCode;
    private final String partnerName;
    private final String apiUrl;
    private final Long countryId;
}
