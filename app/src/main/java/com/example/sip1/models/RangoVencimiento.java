package com.example.sip1.models;

import java.io.Serializable;

public class RangoVencimiento implements Serializable {

    private String rangoVencimiento;
    private int value;

    public String getRangoVencimiento() {
        return rangoVencimiento;
    }

    public void setRangoVencimiento(String rangoVencimiento) {
        this.rangoVencimiento = rangoVencimiento;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}