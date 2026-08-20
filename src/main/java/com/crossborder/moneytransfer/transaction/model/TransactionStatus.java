package com.crossborder.moneytransfer.transaction.model;

/** Defines the lifecycle states of a money-transfer transaction. */
public enum TransactionStatus {
    PENDING,
    IN_PROGRESS,
    SUCCESS,
    FAILED,
    CANCELLED
}
