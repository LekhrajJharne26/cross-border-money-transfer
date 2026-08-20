package com.crossborder.moneytransfer.country.controller;

import com.crossborder.moneytransfer.country.dto.CountryResponse;
import com.crossborder.moneytransfer.country.service.CountryService;
import com.crossborder.moneytransfer.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Secured REST API for browsing active transfer destination countries. */
@RestController @RequestMapping("/v1/countries") @RequiredArgsConstructor
@Tag(name = "Countries", description = "Active destination-country catalogue")
@SecurityRequirement(name = "bearerAuth")
public class CountryController {
    private final CountryService countryService;
    @GetMapping @Operation(summary = "List active countries")
    public ResponseEntity<ApiResponse<List<CountryResponse>>> findActiveCountries() {
        return ResponseEntity.ok(ApiResponse.success("Active countries retrieved successfully", countryService.findActiveCountries()));
    }
}
