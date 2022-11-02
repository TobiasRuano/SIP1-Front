package com.example.sip1.models;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private String name;
    private Double amount;
    private Date nextChargeDate;
    private String category;

    public Expense(String name, Double amount, Date nextChargeDate, String category) {
        this.name = name;
        this.amount = amount;
        this.nextChargeDate = nextChargeDate;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getNextChargeDate() {
        return nextChargeDate;
    }

    public void setNextChargeDate(Date nextChargeDate) {
        this.nextChargeDate = nextChargeDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
