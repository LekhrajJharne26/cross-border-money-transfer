package com.crossborder.moneytransfer.country.service.impl;

import com.crossborder.moneytransfer.country.dto.CountryResponse;
import com.crossborder.moneytransfer.country.repository.CountryRepository;
import com.crossborder.moneytransfer.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Maps active country catalogue entities into API response DTOs. */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    @Override
    public List<CountryResponse> findActiveCountries() {
        return countryRepository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(country -> CountryResponse.builder().id(country.getId()).code(country.getCode()).name(country.getName()).build()).toList();
    }
}
