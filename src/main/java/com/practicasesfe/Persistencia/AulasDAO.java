package com.practicasesfe.Persistencia;
import com.practicasesfe.dominio.Aulas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AulasDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public AulasDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Aulas create(Aulas aula) throws SQLException {
        Aulas res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO aulas (nombre_aula, capacidad) VALUES (?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, aula.getNombre());
            ps.setInt(2, aula.getCapacidad());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating aula failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el aula: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean update(Aulas aula) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE aulas SET nombre_aula = ?, capacidad = ? WHERE id = ?"
            );
            ps.setString(1, aula.getNombre());
            ps.setInt(2, aula.getCapacidad());
            ps.setInt(3, aula.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el aula: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Aulas aula) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM aulas WHERE id = ?");
            ps.setInt(1, aula.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el aula: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Aulas> search(String nombre) throws SQLException {
        ArrayList<Aulas> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre_aula, capacidad FROM aulas WHERE nombre_aula LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Aulas aula = new Aulas();
                aula.setId(rs.getInt(1));
                aula.setNombre(rs.getString(2));
                aula.setCapacidad(rs.getInt(3));
                records.add(aula);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar aulas: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    public Aulas getById(int id) throws SQLException {
        Aulas aula = new Aulas();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre_aula, capacidad FROM aulas WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                aula.setId(rs.getInt(1));
                aula.setNombre(rs.getString(2));
                aula.setCapacidad(rs.getInt(3));
            } else {
                aula = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el aula por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return aula;
    }
}
