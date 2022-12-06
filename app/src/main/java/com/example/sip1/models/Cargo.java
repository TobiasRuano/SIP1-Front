package com.example.sip1.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Cargo {

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

