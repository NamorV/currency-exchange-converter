package com.verkalets.currencyexchangeconverter.controller;

import com.verkalets.currencyexchangeconverter.model.CurrencyInformation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping("/converter")
public class CurrencyExchangeController {
    @GetMapping("/calculate/{fromCode}/{toCode}/{amount}")
    public void showListOfCurrencies(@PathVariable String fromCode,
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
        System.out.println(result);
    }

    private double getCurrencyRate(String code) {
        final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/";
        RestTemplate restTemplate = new RestTemplate();
        CurrencyInformation result = restTemplate.getForObject(uri + code, CurrencyInformation.class);
        return result.getRates().get(0).getMid();
    }
}
