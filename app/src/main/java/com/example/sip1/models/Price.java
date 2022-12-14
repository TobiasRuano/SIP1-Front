package com.example.sip1.models;

import java.io.Serializable;

public class Price implements Serializable {
    private long id;
    private int amount;
    private String description;

    public Price() {
        id = 0;
        amount = 0;
        description = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
