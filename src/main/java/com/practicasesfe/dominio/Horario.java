package com.practicasesfe.dominio;

import java.time.LocalTime;

public class Horario {

    private int idHorario;
    private String nombreHorario;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String dias;

    // Constructor vacío
    public Horario() {}

    // Constructor con todos los campos
    public Horario(int idHorario, String nombreHorario, LocalTime horaInicio, LocalTime horaFin, String dias) {
        this.idHorario = idHorario;
        this.nombreHorario = nombreHorario;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.dias = dias;
    }

    // Constructor sin ID (para inserciones)
    public Horario(String nombreHorario, LocalTime horaInicio, LocalTime horaFin, String dias) {
        this.nombreHorario = nombreHorario;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.dias = dias;
    }


    // Getters y setters
    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getNombreHorario() {
        return nombreHorario;
    }

    public void setNombreHorario(String nombreHorario) {
        this.nombreHorario = nombreHorario;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return nombreHorario + " (" + dias + ", " + horaInicio + " - " + horaFin + ")";
    }


}
