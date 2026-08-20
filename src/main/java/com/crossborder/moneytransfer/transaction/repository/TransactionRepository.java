package com.crossborder.moneytransfer.transaction.repository;

import com.crossborder.moneytransfer.transaction.entity.Transaction;
import com.crossborder.moneytransfer.transaction.model.TransactionStatus;
import com.crossborder.moneytransfer.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/** Persistence gateway for transfer records and sender-scoped history queries. */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrderByCreatedAtDesc(User sender);
    Optional<Transaction> findByTransactionNumber(String transactionNumber);
    List<Transaction> findByStatus(TransactionStatus status);
    Optional<Transaction> findByIdAndSenderId(Long id, Long senderId);
}
