package com.verkalets.currencyexchangeconverter.service;

import com.verkalets.currencyexchangeconverter.model.*;
import com.verkalets.currencyexchangeconverter.repository.CurrencyRepository;
import com.verkalets.currencyexchangeconverter.repository.FunctionCallRepository;
import com.verkalets.currencyexchangeconverter.repository.NbpApiCallsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("exchangeService")
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private NbpApiCallsRepository nbpApiCallsRepository;
    @Autowired
    private FunctionCallRepository functionCallRepository;

    private final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/";

    @Override
    public List<Currency> getAvailableCurrencies() {
        functionCallRepository.save(new FunctionCall(new Date(), FunctionType.GET_CURRENCIES, 200));
        return currencyRepository.findAll();
    }

    @Override
    public ResponseEntity calculate(ExchangeRequestMessage exchangeRequestMessage) {
        if (!validateCurrencies(exchangeRequestMessage.getFromCurrency(), exchangeRequestMessage.getToCurrency())) {
            functionCallRepository.save(new FunctionCall(new Date(), FunctionType.CALCULATE, 404,
                    exchangeRequestMessage.getFromCurrency(), exchangeRequestMessage.getToCurrency(), exchangeRequestMessage.getAmount(),
                    0));
            return ResponseEntity.notFound().build();
        }

        CurrencyInformation fromCurrency;
        CurrencyInformation toCurrency;
        double result;
        if (exchangeRequestMessage.getFromCurrency().equals("PLN")) {
            fromCurrency = getCurrency(exchangeRequestMessage.getToCurrency());
            result = exchangeRequestMessage.getAmount() * fromCurrency.getRates().get(0).getMid();
        } else if (exchangeRequestMessage.getToCurrency().equals("PLN")) {
            toCurrency = getCurrency(exchangeRequestMessage.getFromCurrency());
            result = exchangeRequestMessage.getAmount() / toCurrency.getRates().get(0).getMid();
        } else {
            fromCurrency = getCurrency(exchangeRequestMessage.getToCurrency());
            toCurrency = getCurrency(exchangeRequestMessage.getFromCurrency());
            result = exchangeRequestMessage.getAmount() * fromCurrency.getRates().get(0).getMid()
                    / toCurrency.getRates().get(0).getMid();
        }

        functionCallRepository.save(new FunctionCall(new Date(), FunctionType.CALCULATE, 200,
                exchangeRequestMessage.getFromCurrency(), exchangeRequestMessage.getToCurrency(), exchangeRequestMessage.getAmount(),
                result));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity getExchangeRates(List<String> currencies) {
        if (!validateCurrencies(currencies)) {
            functionCallRepository.save(new FunctionCall(new Date(), FunctionType.GET_EXCHANGE_RATES, 404,
                    currencies.toString()));
            return ResponseEntity.notFound().build();
        }

        List<CurrencyInformation> responseBody = new ArrayList<>();
        currencies.remove("PLN");

        for (String currency : currencies) {
            responseBody.add(getCurrency(currency));
        }

        functionCallRepository.save(new FunctionCall(new Date(), FunctionType.GET_EXCHANGE_RATES, 200,
                currencies.toString()));
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private CurrencyInformation getCurrency(String code) {
        RestTemplate restTemplate = new RestTemplate();
        CurrencyInformation result = restTemplate.getForObject(uri + code, CurrencyInformation.class);
        nbpApiCallsRepository.save(new NbpApiCall(new Date(), code));

        return result;
    }

    private boolean validateCurrencies(List<String> currenciesCodes) {
        List<String> availableCurrenciesCodes = getAvailableCurrenciesCodes();
        for (String code : currenciesCodes) {
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
        for (Currency currency : currencyRepository.findAll()) {
            availableCurrenciesCodes.add(currency.getCode());
        }
        return availableCurrenciesCodes;
    }
}
