package com.lazarilloapp.lazarilloapp.modelado;

import java.io.Serializable;

public class Lugar implements Serializable {
    private String direccion;
    private String etiqueta;

    public Lugar() {

    }

    public Lugar(String direccion, String etiqueta) {
        this.direccion = direccion;
        this.etiqueta = etiqueta;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
