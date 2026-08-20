package com.crossborder.moneytransfer.beneficiary.service;

import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryRequest;
import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryResponse;
import com.crossborder.moneytransfer.user.entity.User;
import java.util.List;

/** Defines owner-scoped beneficiary management use cases. */
public interface BeneficiaryService {
    BeneficiaryResponse create(User owner, BeneficiaryRequest request);
    BeneficiaryResponse update(Long beneficiaryId, Long ownerId, BeneficiaryRequest request);
    void delete(Long beneficiaryId, Long ownerId);
    BeneficiaryResponse findById(Long beneficiaryId, Long ownerId);
    List<BeneficiaryResponse> findAll(Long ownerId);
}
