package com.crossborder.moneytransfer.partner.repository;

import com.crossborder.moneytransfer.partner.entity.BankingPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Persistence gateway for banking partners, filtered by country and active status. */
public interface BankingPartnerRepository extends JpaRepository<BankingPartner, Long> {
    List<BankingPartner> findAllByCountryIdAndActiveTrueOrderByPartnerNameAsc(Long countryId);
}
