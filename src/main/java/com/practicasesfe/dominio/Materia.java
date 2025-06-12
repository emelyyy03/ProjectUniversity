package com.practicasesfe.dominio;

public class Materia {

   private int id_materia;
   private String nombre_materia;
   private String codigo_materia;
   private int uv;
   private int id_docente;

    public Materia() {
    }

    public Materia(int id_materia, String nombre_materia, String codigo_materia, int uv, int id_docente) {
        this.id_materia = id_materia;
        this.nombre_materia = nombre_materia;
        this.codigo_materia = codigo_materia;
        this.uv = uv;
        this.id_docente = id_docente;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public String getNombre_materia() {
        return nombre_materia;
    }

    public void setNombre_materia(String nombre_materia) {
        this.nombre_materia = nombre_materia;
    }

    public String getCodigo_materia() {
        return codigo_materia;
    }

    public void setCodigo_materia(String codigo_materia) {
        this.codigo_materia = codigo_materia;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getId_docente() {
        return id_docente;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }
}
