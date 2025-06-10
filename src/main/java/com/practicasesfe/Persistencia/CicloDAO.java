package com.practicasesfe.Persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.practicasesfe.dominio.Ciclo;

public class CicloDAO {

    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CicloDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Ciclo create(Ciclo ciclo) throws SQLException {
        Ciclo res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO ciclos (nombre_ciclo, fecha_inicio, fecha_fin) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, ciclo.getNombreCiclo());
            ps.setDate(2, ciclo.getFechaInicio());
            ps.setDate(3, ciclo.getFechaFin());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating ciclo failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el ciclo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean update(Ciclo ciclo) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE ciclos SET nombre_ciclo = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_ciclo = ?"
            );
            ps.setString(1, ciclo.getNombreCiclo());
            ps.setDate(2, ciclo.getFechaInicio());
            ps.setDate(3, ciclo.getFechaFin());
            ps.setInt(4, ciclo.getIdCiclo());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el ciclo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }


    public boolean delete(Ciclo ciclo) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM ciclos WHERE id_ciclo = ?");
            ps.setInt(1, ciclo.getIdCiclo());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el ciclo: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }


    public Ciclo getById(int id) throws SQLException {
        Ciclo ciclo = null;
        try {
            ps = conn.connect().prepareStatement("SELECT id_ciclo, nombre_ciclo, fecha_inicio, fecha_fin FROM ciclos WHERE id_ciclo = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                ciclo = new Ciclo();
                ciclo.setIdCiclo(rs.getInt("id_ciclo"));
                ciclo.setNombreCiclo(rs.getString("nombre_ciclo"));
                ciclo.setFechaInicio(rs.getDate("fecha_inicio"));
                ciclo.setFechaFin(rs.getDate("fecha_fin"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener ciclo por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return ciclo;
    }


    public ArrayList<Ciclo> search(String nombre) throws SQLException {
        ArrayList<Ciclo> ciclos = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_ciclo, nombre_ciclo, fecha_inicio, fecha_fin FROM ciclos WHERE nombre_ciclo LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Ciclo ciclo = new Ciclo();
                ciclo.setIdCiclo(rs.getInt("id_ciclo"));
                ciclo.setNombreCiclo(rs.getString("nombre_ciclo"));
                ciclo.setFechaInicio(rs.getDate("fecha_inicio"));
                ciclo.setFechaFin(rs.getDate("fecha_fin"));
                ciclos.add(ciclo);
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar ciclos: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return ciclos;
    }

}
