package com.practicasesfe.dominio;

import java.sql.Date;

public class Ciclo {

    private int idCiclo;
    private String nombreCiclo;
    private Date fechaInicio;
    private Date fechaFin;

    // Constructor vacío
    public Ciclo() {
    }

    // Constructor completo
    public Ciclo(int idCiclo, String nombreCiclo, Date fechaInicio, Date fechaFin) {
        this.idCiclo = idCiclo;
        this.nombreCiclo = nombreCiclo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getNombreCiclo() {
        return nombreCiclo;
    }

    public void setNombreCiclo(String nombreCiclo) {
        this.nombreCiclo = nombreCiclo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return nombreCiclo;
    }
}
