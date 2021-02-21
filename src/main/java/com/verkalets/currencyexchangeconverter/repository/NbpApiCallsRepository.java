package com.verkalets.currencyexchangeconverter.repository;

import com.verkalets.currencyexchangeconverter.model.NbpApiCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NbpApiCallsRepository extends JpaRepository<NbpApiCall, Long> {
}
