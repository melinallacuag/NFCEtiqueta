package com.example.nfcetiqueta.WebApiSVEN.Models;

public class TipoRango {

    private String tiporango;
    private String descripcion;

    public TipoRango(String tiporango,String descripcion) {
        this.tiporango = tiporango;
        this.descripcion = descripcion;
    }

    public String getTiporango() {
        return tiporango;
    }

    public void setTiporango(String tiporango) {
        this.tiporango = tiporango;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
