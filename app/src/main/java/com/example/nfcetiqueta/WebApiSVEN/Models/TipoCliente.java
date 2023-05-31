package com.example.nfcetiqueta.WebApiSVEN.Models;

public class TipoCliente {

    private String id;
    private String descripcion;

    public TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
