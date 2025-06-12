package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.EstudianteHorario;
import com.practicasesfe.dominio.Horario;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteHorarioDAO {

    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public EstudianteHorarioDAO() {
        conn = ConnectionManager.getInstance();
    }

    // Asigna horarios a estudiantes
    public EstudianteHorario asignarHorario(EstudianteHorario eh) throws SQLException {
        EstudianteHorario res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO estudiante_horarios (id_estudiante, id_horario) VALUES (?, ?)"
            );
            ps.setInt(1, eh.getIdEstudiante());
            ps.setInt(2, eh.getIdHorario());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                res = eh;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al asignar horario al estudiante: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }



    // Devuelve los horarios asignados a un estudiante
    public ArrayList<Horario> getHorariosByEstudiante(int idEstudiante) throws SQLException {
        ArrayList<Horario> horarios = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT h.id_horario, h.nombre_horario, h.hora_inicio, h.hora_fin, h.dias " +
                            "FROM horarios h " +
                            "JOIN estudiante_horarios eh ON h.id_horario = eh.id_horario " +
                            "WHERE eh.id_estudiante = ?"
            );
            ps.setInt(1, idEstudiante);
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
            throw new SQLException("Error al obtener horarios por estudiante: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return horarios;
    }

    // Devuelve todos los Id de estudiante asignados a un horario
    public ArrayList<Integer> getEstudiantesByHorario(int idHorario) throws SQLException {
        ArrayList<Integer> estudiantes = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_estudiante FROM estudiante_horarios WHERE id_horario = ?"
            );
            ps.setInt(1, idHorario);
            rs = ps.executeQuery();

            while (rs.next()) {
                estudiantes.add(rs.getInt("id_estudiante"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener estudiantes por horario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return estudiantes;
    }

    // Elimina la asignación de un horario
    public boolean delete(EstudianteHorario eh) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM estudiante_horarios WHERE id_estudiante = ? AND id_horario = ?"
            );
            ps.setInt(1, eh.getIdEstudiante());
            ps.setInt(2, eh.getIdHorario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar asignación de horario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }
}
