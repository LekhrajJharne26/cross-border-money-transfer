package com.crossborder.moneytransfer.transaction.service;

import com.crossborder.moneytransfer.transaction.dto.TransactionRequest;
import com.crossborder.moneytransfer.transaction.dto.TransactionResponse;
import com.crossborder.moneytransfer.user.entity.User;
import java.util.List;

/** Defines sender-scoped transaction initiation and retrieval use cases. */
public interface TransactionService {
    TransactionResponse createTransaction(User sender, TransactionRequest request);
    List<TransactionResponse> getAllTransactionsForLoggedInUser(User sender);
    TransactionResponse getTransactionById(Long transactionId, Long senderId);
}
