package com.verkalets.currencyexchangeconverter.service;

import com.verkalets.currencyexchangeconverter.model.Currency;
import com.verkalets.currencyexchangeconverter.model.ExchangeRequestMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExchangeService {
    List<Currency> getAvailableCurrencies();
    ResponseEntity calculate(ExchangeRequestMessage exchangeRequestMessage);
    ResponseEntity getExchangeRates(List<String> currencies);
}
