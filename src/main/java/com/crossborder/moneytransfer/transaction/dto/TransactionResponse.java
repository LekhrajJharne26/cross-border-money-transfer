package com.crossborder.moneytransfer.transaction.dto;

import com.crossborder.moneytransfer.transaction.model.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.Instant;

/** Read-only transaction view returned exclusively to the sending user. */
@Getter @Builder
public class TransactionResponse {
    private final Long transactionId;
    private final String transactionNumber;
    private final String beneficiaryName;
    private final String partnerName;
    private final BigDecimal amount;
    private final String currency;
    private final BigDecimal exchangeRate;
    private final BigDecimal destinationAmount;
    private final TransactionStatus status;
    private final Instant createdAt;
}
