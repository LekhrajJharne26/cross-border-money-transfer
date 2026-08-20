package com.crossborder.moneytransfer.transaction.service.impl;

import com.crossborder.moneytransfer.beneficiary.entity.Beneficiary;
import com.crossborder.moneytransfer.beneficiary.repository.BeneficiaryRepository;
import com.crossborder.moneytransfer.exception.ResourceNotFoundException;
import com.crossborder.moneytransfer.partner.entity.BankingPartner;
import com.crossborder.moneytransfer.partner.repository.BankingPartnerRepository;
import com.crossborder.moneytransfer.transaction.dto.TransactionRequest;
import com.crossborder.moneytransfer.transaction.dto.TransactionResponse;
import com.crossborder.moneytransfer.transaction.entity.Transaction;
import com.crossborder.moneytransfer.transaction.entity.TransactionNumberSequence;
import com.crossborder.moneytransfer.transaction.model.TransactionStatus;
import com.crossborder.moneytransfer.transaction.repository.TransactionNumberSequenceRepository;
import com.crossborder.moneytransfer.transaction.repository.TransactionRepository;
import com.crossborder.moneytransfer.transaction.service.TransactionService;
import com.crossborder.moneytransfer.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Locale;

/** Validates transaction dependencies, allocates its number, and enforces sender-only access. */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {
    private static final String SEQUENCE_KEY = "TXN";
    private final TransactionRepository transactionRepository;
    private final TransactionNumberSequenceRepository sequenceRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final BankingPartnerRepository bankingPartnerRepository;

    @Override @Transactional
    public TransactionResponse createTransaction(User sender, TransactionRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(request.getBeneficiaryId(), sender.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        BankingPartner partner = bankingPartnerRepository.findById(request.getBankingPartnerId())
                .filter(BankingPartner::isActive).orElseThrow(() -> new ResourceNotFoundException("Active banking partner not found"));
        BigDecimal amount = request.getAmount();
        Transaction transaction = Transaction.builder().transactionNumber(nextTransactionNumber()).sender(sender).beneficiary(beneficiary)
                .bankingPartner(partner).amount(amount).currency(request.getCurrency().trim().toUpperCase(Locale.ROOT))
                .exchangeRate(BigDecimal.ONE).destinationAmount(amount).purpose(request.getPurpose().trim())
                .remarks(normalizeRemarks(request.getRemarks())).status(TransactionStatus.PENDING).build();
        return toResponse(transactionRepository.save(transaction));
    }
    @Override
    public List<TransactionResponse> getAllTransactionsForLoggedInUser(User sender) {
        return transactionRepository.findBySenderOrderByCreatedAtDesc(sender).stream().map(this::toResponse).toList();
    }
    @Override
    public TransactionResponse getTransactionById(Long transactionId, Long senderId) {
        return toResponse(transactionRepository.findByIdAndSenderId(transactionId, senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found")));
    }
    private String nextTransactionNumber() {
        TransactionNumberSequence sequence = sequenceRepository.findBySequenceKeyForUpdate(SEQUENCE_KEY)
                .orElseThrow(() -> new IllegalStateException("Transaction number sequence is unavailable"));
        return "TXN" + Year.now().getValue() + String.format("%06d", sequence.nextValue());
    }
    private String normalizeRemarks(String remarks) { return remarks == null || remarks.isBlank() ? null : remarks.trim(); }
    private TransactionResponse toResponse(Transaction value) {
        return TransactionResponse.builder().transactionId(value.getId()).transactionNumber(value.getTransactionNumber())
                .beneficiaryName(value.getBeneficiary().getFirstName() + " " + value.getBeneficiary().getLastName())
                .partnerName(value.getBankingPartner().getPartnerName()).amount(value.getAmount()).currency(value.getCurrency())
                .exchangeRate(value.getExchangeRate()).destinationAmount(value.getDestinationAmount()).status(value.getStatus()).createdAt(value.getCreatedAt()).build();
    }
}
