package com.example.sip1.models;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private int id;
    private String name;
    private Double amount;
    private Date firstChargeDate;
    private Date lastChargeDate;
    private String category;

    public Expense(int id, String name, Double amount, Date firstChargeDate, Date lastChargeDate, String category) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.firstChargeDate = firstChargeDate;
        this.lastChargeDate = lastChargeDate;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getFirstChargeDate() {
        return firstChargeDate;
    }

    public void setFirstChargeDate(Date firstChargeDate) {
        this.firstChargeDate = firstChargeDate;
    }

    public Date getLastChargeDate() {
        return lastChargeDate;
    }

    public void setLastChargeDate(Date lastChargeDate) {
        this.lastChargeDate = lastChargeDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
