package com.verkalets.currencyexchangeconverter.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FunctionCall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private FunctionType functionType;
    private int responseCode;
    private String searchedCurrencies;
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double result;

    public FunctionCall(Date date, FunctionType functionType, int responseCode) {
        this.date = date;
        this.functionType = functionType;
        this.responseCode = responseCode;
    }

    public FunctionCall(Date date, FunctionType functionType, int responseCode, String searchedCurrencies) {
        this.date = date;
        this.functionType = functionType;
        this.responseCode = responseCode;
        this.searchedCurrencies = searchedCurrencies;
    }

    public FunctionCall(Date date, FunctionType functionType, int responseCode, String fromCurrency, String toCurrency, double amount, double result) {
        this.date = date;
        this.functionType = functionType;
        this.responseCode = responseCode;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.result = result;
    }

    public FunctionCall() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getSearchedCurrencies() {
        return searchedCurrencies;
    }

    public void setSearchedCurrencies(String searchedCurrencies) {
        this.searchedCurrencies = searchedCurrencies;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
