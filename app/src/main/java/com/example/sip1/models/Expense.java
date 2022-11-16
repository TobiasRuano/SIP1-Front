package com.example.sip1.models;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private String name;
    private Double amount;
    private Date nextChargeDate;
    private String category;
    private Usage useAmount;
    private String url;
    private String pasosDesuscripcion;

    public Expense(String name, Double amount, Date nextChargeDate, String category, Usage usage, String url, String pasosDesuscripcion) {
        this.name = name;
        this.amount = amount;
        this.nextChargeDate = nextChargeDate;
        this.category = category;
        this.useAmount = usage;
        this.url = url;
        this.pasosDesuscripcion = pasosDesuscripcion;
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

    public void setUseAmount(Usage useAmount) { this.useAmount = useAmount; }

    public Usage getUseAmount() { return useAmount; }

    public String getPasosDesuscripcion() {
        return pasosDesuscripcion;
    }

    public void setPasosDesuscripcion(String pasosDesuscripcion) {
        this.pasosDesuscripcion = pasosDesuscripcion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
