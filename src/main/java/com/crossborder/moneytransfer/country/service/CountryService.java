package com.crossborder.moneytransfer.country.service;

import com.crossborder.moneytransfer.country.dto.CountryResponse;
import java.util.List;

/** Defines read operations for active destination countries. */
public interface CountryService {
    List<CountryResponse> findActiveCountries();
}
