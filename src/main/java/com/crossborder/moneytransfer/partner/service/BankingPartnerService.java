package com.crossborder.moneytransfer.partner.service;

import com.crossborder.moneytransfer.partner.dto.PartnerResponse;
import java.util.List;

/** Defines read operations for active partners within an active country. */
public interface BankingPartnerService {
    List<PartnerResponse> findActiveByCountryId(Long countryId);
}
