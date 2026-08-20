package com.crossborder.moneytransfer.country.repository;

import com.crossborder.moneytransfer.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/** Persistence gateway for destination-country catalogue data. */
public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findAllByActiveTrueOrderByNameAsc();
    Optional<Country> findByIdAndActiveTrue(Long id);
}
