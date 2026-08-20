package com.crossborder.moneytransfer.transaction.controller;

import com.crossborder.moneytransfer.dto.ApiResponse;
import com.crossborder.moneytransfer.transaction.dto.TransactionRequest;
import com.crossborder.moneytransfer.transaction.dto.TransactionResponse;
import com.crossborder.moneytransfer.transaction.service.TransactionService;
import com.crossborder.moneytransfer.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Secured REST API for initiating transfers and viewing the authenticated sender's history. */
@RestController @RequestMapping("/v1/transactions") @RequiredArgsConstructor @Validated
@Tag(name = "Transactions", description = "Authenticated send-money workflow")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping @Operation(summary = "Create a money transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> create(@AuthenticationPrincipal User sender, @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Transaction created successfully", transactionService.createTransaction(sender, request)));
    }
    @GetMapping @Operation(summary = "List the logged-in user's transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> findAll(@AuthenticationPrincipal User sender) {
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactionService.getAllTransactionsForLoggedInUser(sender)));
    }
    @GetMapping("/{transactionId}") @Operation(summary = "View one of the logged-in user's transactions")
    public ResponseEntity<ApiResponse<TransactionResponse>> findById(@AuthenticationPrincipal User sender, @PathVariable @Positive Long transactionId) {
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", transactionService.getTransactionById(transactionId, sender.getId())));
    }
}
