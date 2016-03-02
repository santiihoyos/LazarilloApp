package com.lazarilloapp.lazarilloapp.modelado;

public class Incidencia {

    private String descripcion;
    private Punto coordenadas;

    public Incidencia(String descripcion, Punto coordenadas) {
        this.descripcion = descripcion;
        this.coordenadas = coordenadas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Punto getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Punto coordenadas) {
        this.coordenadas = coordenadas;
    }
}
