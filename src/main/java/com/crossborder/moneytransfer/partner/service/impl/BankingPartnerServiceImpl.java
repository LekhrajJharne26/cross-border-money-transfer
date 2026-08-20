package com.crossborder.moneytransfer.partner.service.impl;

import com.crossborder.moneytransfer.country.repository.CountryRepository;
import com.crossborder.moneytransfer.exception.ResourceNotFoundException;
import com.crossborder.moneytransfer.partner.dto.PartnerResponse;
import com.crossborder.moneytransfer.partner.repository.BankingPartnerRepository;
import com.crossborder.moneytransfer.partner.service.BankingPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Returns only active partners after ensuring the requested country is active and available. */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class BankingPartnerServiceImpl implements BankingPartnerService {
    private final CountryRepository countryRepository;
    private final BankingPartnerRepository bankingPartnerRepository;
    @Override
    public List<PartnerResponse> findActiveByCountryId(Long countryId) {
        if (countryRepository.findByIdAndActiveTrue(countryId).isEmpty()) throw new ResourceNotFoundException("Active country not found");
        return bankingPartnerRepository.findAllByCountryIdAndActiveTrueOrderByPartnerNameAsc(countryId).stream()
                .map(partner -> PartnerResponse.builder().id(partner.getId()).partnerCode(partner.getPartnerCode())
                        .partnerName(partner.getPartnerName()).apiUrl(partner.getApiUrl()).countryId(partner.getCountry().getId()).build()).toList();
    }
}
