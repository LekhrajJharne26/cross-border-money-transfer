package com.crossborder.moneytransfer.beneficiary.controller;

import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryRequest;
import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryResponse;
import com.crossborder.moneytransfer.beneficiary.service.BeneficiaryService;
import com.crossborder.moneytransfer.dto.ApiResponse;
import com.crossborder.moneytransfer.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Secured REST API for a user to manage only their own beneficiaries. */
@RestController @RequestMapping("/v1/beneficiaries") @RequiredArgsConstructor
@Tag(name = "Beneficiaries", description = "Authenticated beneficiary management")
@SecurityRequirement(name = "bearerAuth")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;
    @PostMapping @Operation(summary = "Add a beneficiary")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> create(@AuthenticationPrincipal User user, @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Beneficiary created successfully", beneficiaryService.create(user, request)));
    }
    @GetMapping @Operation(summary = "View all own beneficiaries")
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> findAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Beneficiaries retrieved successfully", beneficiaryService.findAll(user.getId())));
    }
    @GetMapping("/{beneficiaryId}") @Operation(summary = "View one beneficiary")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> findById(@AuthenticationPrincipal User user, @PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(ApiResponse.success("Beneficiary retrieved successfully", beneficiaryService.findById(beneficiaryId, user.getId())));
    }
    @PutMapping("/{beneficiaryId}") @Operation(summary = "Update a beneficiary")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> update(@AuthenticationPrincipal User user, @PathVariable Long beneficiaryId, @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Beneficiary updated successfully", beneficiaryService.update(beneficiaryId, user.getId(), request)));
    }
    @DeleteMapping("/{beneficiaryId}") @Operation(summary = "Delete a beneficiary")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal User user, @PathVariable Long beneficiaryId) {
        beneficiaryService.delete(beneficiaryId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Beneficiary deleted successfully", null));
    }
}
