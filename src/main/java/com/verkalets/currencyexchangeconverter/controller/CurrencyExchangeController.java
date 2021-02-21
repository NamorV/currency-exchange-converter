package com.verkalets.currencyexchangeconverter.controller;

import com.verkalets.currencyexchangeconverter.model.Currency;
import com.verkalets.currencyexchangeconverter.model.CurrencyInformation;
import com.verkalets.currencyexchangeconverter.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/converter")
public class CurrencyExchangeController {

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/getCurrencies")
    public ResponseEntity showListOfCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(currencies);
    }

    @GetMapping("/calculate/{fromCode}/{toCode}/{amount}")
    public ResponseEntity calculate(@PathVariable String fromCode,
                                     @PathVariable String toCode,
                                     @PathVariable double amount ) {
        double result;
        if(fromCode.equals("PLN")) {
            result =  amount / getCurrencyRate(toCode);
        } else if(toCode.equals("PLN")) {
            result = amount * getCurrencyRate(fromCode);
        } else {
            result = amount * (getCurrencyRate(fromCode) / getCurrencyRate(toCode));
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/getExchangeRates")
    public ResponseEntity showExchangeRates(@RequestBody List<String> currencies) {
        List<CurrencyInformation> responseBody = new ArrayList<>();
        currencies.remove("PLN");
        final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/";
        RestTemplate restTemplate = new RestTemplate();

        for (String currency : currencies) {
            responseBody.add(restTemplate.getForObject(uri + currency, CurrencyInformation.class));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private double getCurrencyRate(String code) {
        final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/";
        RestTemplate restTemplate = new RestTemplate();
        CurrencyInformation result = restTemplate.getForObject(uri + code, CurrencyInformation.class);
        return result.getRates().get(0).getMid();
    }
}
