package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Estudiantes;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class EstudiantesDAO {
    //Propiedades
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    //Instancia
    public EstudiantesDAO() {
        conn = ConnectionManager.getInstance();
    }

    //Metodo Crear
    public Estudiantes create(Estudiantes estudiante) throws SQLException {
        Estudiantes res = null;
        try {
            PreparedStatement  ps = conn.connect().prepareStatement(
                    "INSERT INTO estudiantes (carnet, nombre_completo, carrera, fecha_ingreso, promedio_notas, modalidad_estudio, id_usuario) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, estudiante.getCarnet());
            ps.setString(2, estudiante.getNombreCompleto());
            ps.setString(3, estudiante.getCarrera());
            ps.setTimestamp(4, estudiante.getFechaIngreso() != null ? Timestamp.valueOf(estudiante.getFechaIngreso()) : null);
            ps.setBigDecimal(5, estudiante.getPromedioNotas());
            ps.setString(6, estudiante.getModalidadEstudio());
            if (estudiante.getIdUsuario() != null) {
                ps.setInt(7, estudiante.getIdUsuario());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating estudiante failed, no ID obtained.");
                }
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el estudiante: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    //Metodo Actualizar
    public boolean update(Estudiantes estudiante) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE estudiantes SET carnet = ?, nombre_completo = ?, carrera = ?, fecha_ingreso = ?, " +
                            "promedio_notas = ?, modalidad_estudio = ?, id_usuario = ? WHERE id_estudiante = ?"
            );
            ps.setString(1, estudiante.getCarnet());
            ps.setString(2, estudiante.getNombreCompleto());
            ps.setString(3, estudiante.getCarrera());
            ps.setTimestamp(4, estudiante.getFechaIngreso() != null ? Timestamp.valueOf(estudiante.getFechaIngreso()) : null);
            ps.setBigDecimal(5, estudiante.getPromedioNotas());
            ps.setString(6, estudiante.getModalidadEstudio());
            if (estudiante.getIdUsuario() != null) {
                ps.setInt(7, estudiante.getIdUsuario());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, estudiante.getIdEstudiante());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el estudiante: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    //Metodo Eliminar
    public boolean delete(Estudiantes estudiante) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM estudiantes WHERE id_estudiante = ?");
            ps.setInt(1, estudiante.getIdEstudiante());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el estudiante: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    //Metodo Obtener por Id
    public Estudiantes getById(int id) throws SQLException {
        Estudiantes estudiante = null;
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_estudiante, carnet, nombre_completo, carrera, fecha_ingreso, promedio_notas, modalidad_estudio, id_usuario " +
                            "FROM estudiantes WHERE id_estudiante = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                estudiante = new Estudiantes();
                estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                estudiante.setCarnet(rs.getString("carnet"));
                estudiante.setNombreCompleto(rs.getString("nombre_completo"));
                estudiante.setCarrera(rs.getString("carrera"));
                Timestamp ts = rs.getTimestamp("fecha_ingreso");
                estudiante.setFechaIngreso(ts != null ? ts.toLocalDateTime() : null);
                estudiante.setPromedioNotas(rs.getBigDecimal("promedio_notas"));
                estudiante.setModalidadEstudio(rs.getString("modalidad_estudio"));
                int idUsuario = rs.getInt("id_usuario");
                estudiante.setIdUsuario(rs.wasNull() ? null : idUsuario);
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener estudiante por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return estudiante;
    }

    //Metodo Buscar
    public ArrayList<Estudiantes> search(String nombre) throws SQLException {
        ArrayList<Estudiantes> estudiantes = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_estudiante, carnet, nombre_completo, carrera, fecha_ingreso, promedio_notas, modalidad_estudio, id_usuario " +
                            "FROM estudiantes WHERE nombre_completo LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Estudiantes estudiante = new Estudiantes();
                estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                estudiante.setCarnet(rs.getString("carnet"));
                estudiante.setNombreCompleto(rs.getString("nombre_completo"));
                estudiante.setCarrera(rs.getString("carrera"));
                Timestamp ts = rs.getTimestamp("fecha_ingreso");
                estudiante.setFechaIngreso(ts != null ? ts.toLocalDateTime() : null);
                estudiante.setPromedioNotas(rs.getBigDecimal("promedio_notas"));
                estudiante.setModalidadEstudio(rs.getString("modalidad_estudio"));
                int idUsuario = rs.getInt("id_usuario");
                estudiante.setIdUsuario(rs.wasNull() ? null : idUsuario);
                estudiantes.add(estudiante);
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar estudiantes: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return estudiantes;
    }
}
