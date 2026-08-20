package com.crossborder.moneytransfer.transaction.repository;

import com.crossborder.moneytransfer.transaction.entity.TransactionNumberSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

/** Provides a pessimistically locked counter for collision-free transaction-number allocation. */
public interface TransactionNumberSequenceRepository extends JpaRepository<TransactionNumberSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sequence from TransactionNumberSequence sequence where sequence.sequenceKey = :sequenceKey")
    Optional<TransactionNumberSequence> findBySequenceKeyForUpdate(String sequenceKey);
}
