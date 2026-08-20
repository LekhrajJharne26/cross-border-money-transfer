package com.crossborder.moneytransfer.beneficiary.service.impl;

import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryRequest;
import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryResponse;
import com.crossborder.moneytransfer.beneficiary.entity.Beneficiary;
import com.crossborder.moneytransfer.beneficiary.repository.BeneficiaryRepository;
import com.crossborder.moneytransfer.beneficiary.service.BeneficiaryService;
import com.crossborder.moneytransfer.exception.ResourceNotFoundException;
import com.crossborder.moneytransfer.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Implements CRUD operations and enforces beneficiary ownership at the data-access boundary. */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;

    @Override @Transactional
    public BeneficiaryResponse create(User owner, BeneficiaryRequest request) {
        Beneficiary beneficiary = Beneficiary.builder().user(owner).build();
        beneficiary.update(request);
        return toResponse(beneficiaryRepository.save(beneficiary));
    }
    @Override @Transactional
    public BeneficiaryResponse update(Long beneficiaryId, Long ownerId, BeneficiaryRequest request) {
        Beneficiary beneficiary = ownedBeneficiary(beneficiaryId, ownerId);
        beneficiary.update(request);
        return toResponse(beneficiary);
    }
    @Override @Transactional
    public void delete(Long beneficiaryId, Long ownerId) { beneficiaryRepository.delete(ownedBeneficiary(beneficiaryId, ownerId)); }
    @Override
    public BeneficiaryResponse findById(Long beneficiaryId, Long ownerId) { return toResponse(ownedBeneficiary(beneficiaryId, ownerId)); }
    @Override
    public List<BeneficiaryResponse> findAll(Long ownerId) { return beneficiaryRepository.findAllByUserIdOrderByCreatedAtDesc(ownerId).stream().map(this::toResponse).toList(); }

    private Beneficiary ownedBeneficiary(Long id, Long ownerId) {
        return beneficiaryRepository.findByIdAndUserId(id, ownerId).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
    }
    private BeneficiaryResponse toResponse(Beneficiary value) {
        return BeneficiaryResponse.builder().id(value.getId()).firstName(value.getFirstName()).lastName(value.getLastName())
                .mobileNumber(value.getMobileNumber()).email(value.getEmail()).address(value.getAddress()).city(value.getCity())
                .state(value.getState()).postalCode(value.getPostalCode()).country(value.getCountry())
                .governmentIdNumber(value.getGovernmentIdNumber()).relationship(value.getRelationship()).createdAt(value.getCreatedAt()).updatedAt(value.getUpdatedAt()).build();
    }
}
