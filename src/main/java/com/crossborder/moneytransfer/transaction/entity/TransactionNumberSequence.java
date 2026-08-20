package com.crossborder.moneytransfer.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Lockable database counter used to generate unique sequential transaction numbers. */
@Entity @Table(name = "transaction_number_sequence") @Getter @NoArgsConstructor
public class TransactionNumberSequence {
    @Id @Column(name = "sequence_key", length = 20) private String sequenceKey;
    @Column(name = "sequence_value", nullable = false) private long lastValue;
    public long nextValue() { return ++lastValue; }
}
