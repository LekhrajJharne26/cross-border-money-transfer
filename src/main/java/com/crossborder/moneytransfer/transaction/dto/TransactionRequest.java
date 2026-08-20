package com.crossborder.moneytransfer.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/** Validated input for a user to initiate a money transfer. */
@Getter @Setter @NoArgsConstructor
public class TransactionRequest {
    @NotNull @Positive private Long beneficiaryId;
    @NotNull @Positive private Long bankingPartnerId;
    @NotNull @Positive private BigDecimal amount;
    @NotBlank @Pattern(regexp = "^[A-Za-z]{3}$", message = "currency must be a three-letter ISO code") private String currency;
    @NotBlank @Size(max = 150) private String purpose;
    @Size(max = 500) private String remarks;
}
