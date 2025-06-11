package com.practicasesfe.dominio;

public class Aulas {
    private int id;
    private String nombre;
    private int capacidad;

    public Aulas() {
    }

    public Aulas(int id, String nombre, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    // Método toString
    @Override
    public String toString() {
        return "Aula {" +
                "ID=" + id +
                ", Nombre='" + nombre + '\'' +
                ", Capacidad=" + capacidad +
                '}';
    }
}
