package com.verkalets.currencyexchangeconverter.repository;

import com.verkalets.currencyexchangeconverter.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
