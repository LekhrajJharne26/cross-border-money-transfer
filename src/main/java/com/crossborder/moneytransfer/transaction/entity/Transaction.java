package com.crossborder.moneytransfer.transaction.entity;

import com.crossborder.moneytransfer.beneficiary.entity.Beneficiary;
import com.crossborder.moneytransfer.partner.entity.BankingPartner;
import com.crossborder.moneytransfer.transaction.model.TransactionStatus;
import com.crossborder.moneytransfer.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

/** JPA record of one user-initiated cross-border money transfer. */
@Entity @Table(name = "transactions") @Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, updatable = false, length = 20) private String transactionNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "sender_id", nullable = false, updatable = false) private User sender;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "beneficiary_id", nullable = false, updatable = false) private Beneficiary beneficiary;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "banking_partner_id", nullable = false, updatable = false) private BankingPartner bankingPartner;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Column(nullable = false, precision = 19, scale = 6) private BigDecimal exchangeRate;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal destinationAmount;
    @Column(nullable = false, length = 150) private String purpose;
    @Column(length = 500) private String remarks;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private TransactionStatus status;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    @PrePersist void initializeTimestamps() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updateTimestamp() { updatedAt = Instant.now(); }
}
