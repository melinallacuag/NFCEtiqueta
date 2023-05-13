package com.example.nfcetiqueta;

public class TipoCliente {

    private String tipocliente;
    private String descripcion;

    public TipoCliente(String tipocliente,String descripcion) {
        this.tipocliente = tipocliente;
        this.descripcion = descripcion;
    }

    public String getTipocliente() {
        return tipocliente;
    }

    public void setTipocliente(String tipocliente) {
        this.tipocliente = tipocliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
