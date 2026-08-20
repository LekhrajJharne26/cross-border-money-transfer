package com.crossborder.moneytransfer.partner.entity;

import com.crossborder.moneytransfer.country.entity.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** JPA catalogue entry for a banking integration available in a destination country. */
@Entity @Table(name = "banking_partners") @Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class BankingPartner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 50) private String partnerCode;
    @Column(nullable = false, length = 150) private String partnerName;
    @Column(nullable = false, length = 500) private String apiUrl;
    @Column(nullable = false) private boolean active;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
