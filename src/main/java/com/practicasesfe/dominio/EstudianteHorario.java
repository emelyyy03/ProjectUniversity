package com.practicasesfe.dominio;

public class EstudianteHorario {


    private int idEstudiante;
    private int idHorario;

    // Constructor vacío
    public EstudianteHorario() {
    }

    // Constructor completo
    public EstudianteHorario(int idEstudiante, int idHorario) {
        this.idEstudiante = idEstudiante;
        this.idHorario = idHorario;
    }

    // Getters y Setters
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    @Override
    public String toString() {
        return "EstudianteHorario{" +
                "idEstudiante=" + idEstudiante +
                ", idHorario=" + idHorario +
                '}';
    }
}
