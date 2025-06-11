package com.practicasesfe.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Estudiantes {
    //Propiedades
    private int idEstudiante;
    private String carnet;
    private String nombreCompleto;
    private String carrera;
    private LocalDateTime fechaIngreso;
    private BigDecimal promedioNotas;
    private String modalidadEstudio;
    private Integer idUsuario; // Puede ser null
    private List<Horario> horarios; // Opcional, si deseas incluir los horarios asignados

    //Constructor vacío
    public Estudiantes() {
    }

    //Constructor lleno
    public Estudiantes(int idEstudiante, String carnet, String nombreCompleto, String carrera,
                       LocalDateTime fechaIngreso, BigDecimal promedioNotas, String modalidadEstudio,
                       Integer idUsuario) {
        this.idEstudiante = idEstudiante;
        this.carnet = carnet;
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
        this.fechaIngreso = fechaIngreso;
        this.promedioNotas = promedioNotas;
        this.modalidadEstudio = modalidadEstudio;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getPromedioNotas() {
        return promedioNotas;
    }

    public void setPromedioNotas(BigDecimal promedioNotas) {
        this.promedioNotas = promedioNotas;
    }

    public String getModalidadEstudio() {
        return modalidadEstudio;
    }

    public void setModalidadEstudio(String modalidadEstudio) {
        this.modalidadEstudio = modalidadEstudio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudiante=" + idEstudiante +
                ", carnet='" + carnet + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", carrera='" + carrera + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", promedioNotas=" + promedioNotas +
                ", modalidadEstudio='" + modalidadEstudio + '\'' +
                ", idUsuario=" + idUsuario +
                ", horarios=" + (horarios != null ? horarios.size() : 0) +
                '}';
    }


}
