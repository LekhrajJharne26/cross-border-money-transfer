package com.crossborder.moneytransfer.country.dto;

import lombok.Builder;
import lombok.Getter;

/** Read-only country representation exposed by the destination catalogue API. */
@Getter @Builder
public class CountryResponse {
    private final Long id;
    private final String code;
    private final String name;
}
