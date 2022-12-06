package com.example.sip1.models;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private String name;
    private Price price;
    private Date nextChargeDate;
    private String category;
    private Usage useAmount;
    private String url;
    private String pasosDesubscripcion;

    public Expense(String name, Price price, Date nextChargeDate, String category, Usage usage, String url, String pasosDesubscripcion) {
        this.name = name;
        this.price = price;
        this.nextChargeDate = nextChargeDate;
        this.category = category;
        this.useAmount = usage;
        this.url = url;
        this.pasosDesubscripcion = pasosDesubscripcion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getAmount() {
        return price;
    }

    public void setAmount(Price price) {
        this.price = price;
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
        return pasosDesubscripcion;
    }

    public void setPasosDesuscripcion(String pasosDesuscripcion) {
        this.pasosDesubscripcion = pasosDesuscripcion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
