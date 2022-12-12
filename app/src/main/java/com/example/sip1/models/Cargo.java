package com.example.sip1.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class Cargo implements Serializable {

    @Expose
    public String categoria;
    @Expose
    public String nombre;
    @Expose
    public String pasosDesubscripcion;
    @Expose
    public ArrayList<Price> precios = null;
    @Expose
    public String url;

}

