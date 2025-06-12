package com.practicasesfe.dominio;

import java.sql.Date;

public class Docente {

    private int id_docente;
    private int id_usuario;
    private String nombre_completo;
    private String titulo;
    private double experiencia_anios;
    private Date fecha_contratacion;

    public Docente() {
    }

    public Docente(int id_docente, int id_usuario, String nombre_completo, String titulo, double experiencia_anios, Date fecha_contratacion) {
        this.id_docente = id_docente;
        this.id_usuario = id_usuario;
        this.nombre_completo = nombre_completo;
        this.titulo = titulo;
        this.experiencia_anios = experiencia_anios;
        this.fecha_contratacion = fecha_contratacion;
    }

    public int getId_docente() {
        return id_docente;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getExperiencia_anios() {
        return experiencia_anios;
    }

    public void setExperiencia_anios(double experiencia_anios) {
        this.experiencia_anios = experiencia_anios;
    }

    public Date getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }
}
