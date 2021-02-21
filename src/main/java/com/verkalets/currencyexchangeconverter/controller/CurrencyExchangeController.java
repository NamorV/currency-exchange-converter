package com.verkalets.currencyexchangeconverter.controller;

import com.verkalets.currencyexchangeconverter.model.ExchangeRequestMessage;
import com.verkalets.currencyexchangeconverter.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/converter")
public class CurrencyExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/getCurrencies")
    public ResponseEntity showListOfCurrencies() {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.getAvailableCurrencies());
    }

    @GetMapping("/calculate")
    public ResponseEntity calculate(@RequestBody ExchangeRequestMessage requestBody) {
        return exchangeService.calculate(requestBody);
    }

    @GetMapping("/getExchangeRates")
    public ResponseEntity showExchangeRates(@RequestBody List<String> currencies) {
        return exchangeService.getExchangeRates(currencies);
    }


}
