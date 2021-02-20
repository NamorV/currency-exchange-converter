package com.verkalets.currencyexchangeconverter.model;

import java.util.Date;

public class Rate {
    private Date effectiveDate;
    private double mid;

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }
}
