package com.verkalets.currencyexchangeconverter.service;

import com.verkalets.currencyexchangeconverter.model.Currency;
import com.verkalets.currencyexchangeconverter.model.CurrencyInformation;
import com.verkalets.currencyexchangeconverter.model.ExchangeRequestMessage;
import com.verkalets.currencyexchangeconverter.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("exchangeService")
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private CurrencyRepository currencyRepository;

    private final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/";

    @Override
    public List<Currency> getAvailableCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public ResponseEntity calculate(ExchangeRequestMessage exchangeRequestMessage) {
        if (!validateCurrencies(exchangeRequestMessage.getFromCurrency(), exchangeRequestMessage.getToCurrency())) {
            return ResponseEntity.notFound().build();
        }

        double result = 0;
        if (exchangeRequestMessage.getFromCurrency().equals("PLN")) {
            result = exchangeRequestMessage.getAmount() * getCurrencyRate(exchangeRequestMessage.getToCurrency());
        } else if (exchangeRequestMessage.getToCurrency().equals("PLN")) {
            result = exchangeRequestMessage.getAmount() / getCurrencyRate(exchangeRequestMessage.getFromCurrency());
        } else {
            result = exchangeRequestMessage.getAmount() * getCurrencyRate(exchangeRequestMessage.getFromCurrency())
                    / getCurrencyRate(exchangeRequestMessage.getToCurrency());
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity getExchangeRates(List<String> currencies) {
        if (!validateCurrencies(currencies)) {
            return ResponseEntity.notFound().build();
        }

        List<CurrencyInformation> responseBody = new ArrayList<>();
        currencies.remove("PLN");

        RestTemplate restTemplate = new RestTemplate();

        for (String currency : currencies) {
            responseBody.add(restTemplate.getForObject(uri + currency, CurrencyInformation.class));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private double getCurrencyRate(String code) {
        RestTemplate restTemplate = new RestTemplate();
        CurrencyInformation result = restTemplate.getForObject(uri + code, CurrencyInformation.class);
        return result.getRates().get(0).getMid();
    }

    private boolean validateCurrencies(List<String> currenciesCodes) {
        List<String> availableCurrenciesCodes = getAvailableCurrenciesCodes();
        for (String code : availableCurrenciesCodes) {
            if (!availableCurrenciesCodes.contains(code)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateCurrencies(String fromCurrencyCode, String toCurrencyCode) {
        List<String> availableCurrenciesCodes = getAvailableCurrenciesCodes();
        return availableCurrenciesCodes.contains(fromCurrencyCode) && availableCurrenciesCodes.contains(toCurrencyCode);
    }

    private List<String> getAvailableCurrenciesCodes() {
        List<String> availableCurrenciesCodes = new ArrayList<>();
        for (Currency currency : getAvailableCurrencies()) {
            availableCurrenciesCodes.add(currency.getCode());
        }
        return availableCurrenciesCodes;
    }
}
