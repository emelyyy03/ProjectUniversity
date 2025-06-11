package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Horario;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class HorarioDAO {

//Variables que se utilizaran
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;
//Instancia
    public HorarioDAO() {
        conn = ConnectionManager.getInstance();
    }

//Metodo para crear un nuevo horario
    public Horario create(Horario horario) throws SQLException {
        Horario res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO horarios (nombre_horario, hora_inicio, hora_fin, dias) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, horario.getNombreHorario());
            ps.setTime(2, Time.valueOf(horario.getHoraInicio()));
            ps.setTime(3, Time.valueOf(horario.getHoraFin()));
            ps.setString(4, horario.getDias());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating horario failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el horario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

//Metodo para editar un  horario
    public boolean update(Horario horario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE horarios SET nombre_horario = ?, hora_inicio = ?, hora_fin = ?, dias = ? WHERE id_horario = ?"
            );
            ps.setString(1, horario.getNombreHorario());
            ps.setTime(2, Time.valueOf(horario.getHoraInicio()));
            ps.setTime(3, Time.valueOf(horario.getHoraFin()));
            ps.setString(4, horario.getDias());
            ps.setInt(5, horario.getIdHorario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el horario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

//Metodo para eliminar un horario
    public boolean delete(Horario horario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM horarios WHERE id_horario = ?");
            ps.setInt(1, horario.getIdHorario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el horario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }


    //Metodo para obtener un id de un horario
    public Horario getById(int id) throws SQLException {
        Horario horario = null;
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_horario, nombre_horario, hora_inicio, hora_fin, dias FROM horarios WHERE id_horario = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                horario = new Horario();
                horario.setIdHorario(rs.getInt("id_horario"));
                horario.setNombreHorario(rs.getString("nombre_horario"));
                horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                horario.setDias(rs.getString("dias"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener horario por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return horario;
    }

//Metodo para buscar un horario
    public ArrayList<Horario> search(String term) throws SQLException {
        ArrayList<Horario> horarios = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_horario, nombre_horario, hora_inicio, hora_fin, dias FROM horarios WHERE nombre_horario LIKE ? OR dias LIKE ?"
            );
            ps.setString(1, "%" + term + "%");
            ps.setString(2, "%" + term + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Horario horario = new Horario();
                horario.setIdHorario(rs.getInt("id_horario"));
                horario.setNombreHorario(rs.getString("nombre_horario"));
                horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                horario.setDias(rs.getString("dias"));
                horarios.add(horario);
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar horarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return horarios;
    }



}
