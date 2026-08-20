package com.crossborder.moneytransfer.beneficiary.repository;

import com.crossborder.moneytransfer.beneficiary.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/** Persistence gateway that always queries beneficiaries in their owner's scope. */
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    Optional<Beneficiary> findByIdAndUserId(Long id, Long userId);
    List<Beneficiary> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
