package com.crossborder.moneytransfer.beneficiary.entity;

import com.crossborder.moneytransfer.beneficiary.dto.BeneficiaryRequest;
import com.crossborder.moneytransfer.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Locale;

/** JPA entity for a transfer recipient, owned by exactly one platform user. */
@Entity @Table(name = "beneficiaries") @Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Beneficiary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
    @Column(nullable = false, length = 100) private String firstName;
    @Column(nullable = false, length = 100) private String lastName;
    @Column(nullable = false, length = 30) private String mobileNumber;
    @Column(nullable = false, length = 254) private String email;
    @Column(nullable = false, length = 255) private String address;
    @Column(nullable = false, length = 100) private String city;
    @Column(nullable = false, length = 100) private String state;
    @Column(nullable = false, length = 20) private String postalCode;
    @Column(nullable = false, length = 100) private String country;
    @Column(nullable = false, length = 100) private String governmentIdNumber;
    @Column(nullable = false, length = 100) private String relationship;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    /** Applies a validated request without allowing the record owner to be changed. */
    public void update(BeneficiaryRequest request) {
        this.firstName = request.getFirstName().trim(); this.lastName = request.getLastName().trim();
        this.mobileNumber = request.getMobileNumber().trim(); this.email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        this.address = request.getAddress().trim(); this.city = request.getCity().trim(); this.state = request.getState().trim();
        this.postalCode = request.getPostalCode().trim(); this.country = request.getCountry().trim();
        this.governmentIdNumber = request.getGovernmentIdNumber().trim(); this.relationship = request.getRelationship().trim();
    }
    @PrePersist void initializeTimestamps() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updateTimestamp() { updatedAt = Instant.now(); }
}
