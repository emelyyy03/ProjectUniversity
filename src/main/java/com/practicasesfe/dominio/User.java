package com.practicasesfe.dominio;

import java.time.LocalDateTime;
import java.sql.Date;

public class User {

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String rol;
    private Date fecha_creacion;
    private Date fecha_act;

    public User() {
    }

    public User(int id, String name, String email, String passwordHash, String rol, Date fecha_creacion, Date fecha_act) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.fecha_creacion = fecha_creacion;
        this.fecha_act = fecha_act;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date getFecha_act() {
        return fecha_act;
    }

    public void setFecha_act(Date fecha_act) {
        this.fecha_act = fecha_act;
    }
}
