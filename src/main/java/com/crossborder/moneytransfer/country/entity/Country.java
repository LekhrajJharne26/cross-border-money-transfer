package com.crossborder.moneytransfer.country.entity;

import com.crossborder.moneytransfer.partner.entity.BankingPartner;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/** JPA catalogue entry for a destination country and its supported banking partners. */
@Entity @Table(name = "countries") @Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Country {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 3) private String code;
    @Column(nullable = false, unique = true, length = 100) private String name;
    @Column(nullable = false) private boolean active;
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @Builder.Default private List<BankingPartner> bankingPartners = new ArrayList<>();
}
