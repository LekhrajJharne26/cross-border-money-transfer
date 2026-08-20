package com.crossborder.moneytransfer.partner.controller;

import com.crossborder.moneytransfer.dto.ApiResponse;
import com.crossborder.moneytransfer.partner.dto.PartnerResponse;
import com.crossborder.moneytransfer.partner.service.BankingPartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Secured REST API for browsing active banking partners within a country. */
@RestController @RequestMapping("/v1/partners") @RequiredArgsConstructor @Validated
@Tag(name = "Banking Partners", description = "Active partner catalogue by country")
@SecurityRequirement(name = "bearerAuth")
public class BankingPartnerController {
    private final BankingPartnerService bankingPartnerService;
    @GetMapping("/country/{countryId}") @Operation(summary = "List active partners for a country")
    public ResponseEntity<ApiResponse<List<PartnerResponse>>> findActiveByCountry(@PathVariable @Positive Long countryId) {
        return ResponseEntity.ok(ApiResponse.success("Active banking partners retrieved successfully", bankingPartnerService.findActiveByCountryId(countryId)));
    }
}
