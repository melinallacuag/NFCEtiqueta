package com.example.nfcetiqueta.WebApiSVEN.Models;

public class TipoDescuento {

    private String tipodescuento;
    private String descripcion;

    public TipoDescuento(String tipodescuento,String descripcion) {
        this.tipodescuento = tipodescuento;
        this.descripcion = descripcion;
    }

    public String getTipodescuento() {
        return tipodescuento;
    }

    public void setTipodescuento(String tipodescuento) {
        this.tipodescuento = tipodescuento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
