package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Docente;
import com.practicasesfe.dominio.Materia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MateriasDAO {

    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    public MateriasDAO(){
        conn = ConnectionManager.getInstance();
    }

    public Materia create(Materia materia) throws SQLException {
        Materia res = null; // Variable para almacenar el usuario creado que se retornará.
        try{
            // Preparar la sentencia SQL para la inserción de un nuevo usuario.
            // Se especifica que se retornen las claves generadas automáticamente.
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "materias (nombre_materia, codigo_materia, uv, id_docente) " +
                            "VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, materia.getNombre_materia()); // Asignar el nombre de la materia.
            ps.setString(2, materia.getCodigo_materia()); //Codio de la materia
            ps.setInt(3, materia.getUv()); //Asignar Uv de la materia
            ps.setInt(4, materia.getId_docente()); //Asigna Id del docente que imparte la materia


            // Ejecutar la sentencia de inserción y obtener el número de filas afectadas.
            int affectedRows = ps.executeUpdate();

            // Verificar si la inserción fue exitosa (al menos una fila afectada).
            if (affectedRows != 0) {
                // Obtener las claves generadas automáticamente por la base de datos (en este caso, el ID).
                ResultSet  generatedKeys = ps.getGeneratedKeys();
                // Mover el cursor al primer resultado (si existe).
                if (generatedKeys.next()) {
                    // Obtener el ID generado. Generalmente la primera columna contiene la clave primaria.
                    int idGenerado= generatedKeys.getInt(1);
                    // Recuperar el usuario completo utilizando el ID generado.
                    res = getById(idGenerado);
                } else {
                    // Lanzar una excepción si la creación del usuario falló y no se obtuvo un ID.
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return res; // Retornar la amteria creada (con su ID asignado) o null si hubo un error.
    }


    public boolean update(Materia materia) throws SQLException{
        boolean res = false; // Variable para indicar si la actualización fue exitosa.
        try{
            // Preparar la sentencia SQL para actualizar la información de una materia
            ps = conn.connect().prepareStatement(
                    "UPDATE materias " +
                            "SET nombre_materia = ?, codigo_materia = ?, uv = ?, id_docente = ? " +
                            "WHERE id_materia = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, materia.getNombre_materia()); // Asignar el nuevo nombre de materia.
            ps.setString(2, materia.getCodigo_materia()); //Asigna el nuevo codigo de materia
            ps.setInt(3, materia.getUv()); //Asignar nuevo uv de materia
            ps.setInt(4, materia.getId_docente());       // Establecer la condición WHERE para identificar la materia a actualizar por su ID.
            ps.setInt(5, materia.getId_materia());

            // Ejecutar la sentencia de actualización y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la actualización fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de actualización.
    }


    public boolean delete(Materia materia) throws SQLException{
        boolean res = false; // Variable para indicar si la eliminación fue exitosa.
        try{
            // Preparar la sentencia SQL para eliminar la materia por su ID.
            ps = conn.connect().prepareStatement(
                    "DELETE FROM materias WHERE id_materia = ?"
            );
            // Establecer el valor del parámetro en la sentencia preparada (el ID dela materia a eliminar).
            ps.setInt(1, materia.getId_materia());

            // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la eliminación fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al eliminar la amateria: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de eliminación.
    }


    public ArrayList<Materia> search(String name) throws SQLException{
        ArrayList<Materia> records  = new ArrayList<>(); // Lista para almacenar las materias encontradas.

        try {
            // Preparar la sentencia SQL para buscar materias por nombre (usando LIKE para búsqueda parcial).
            ps = conn.connect().prepareStatement("SELECT id_materia, nombre_materia, codigo_materia, uv, id_docente " +
                    "FROM materias " +
                    "WHERE nombre_materia LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'name' en cualquier parte del nombre del usuario.
            ps.setString(1, "%" + name + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()){
                // Crear un nuevo objeto User para cada registro encontrado.
                Materia materia = new Materia();
                // Asignar los valores de las columnas a los atributos del objeto User.
                materia.setId_materia(rs.getInt(1));       // Obtener el ID de materia.
                materia.setNombre_materia(rs.getString(2));   // Obtener el nombre de la materia.
                materia.setCodigo_materia(rs.getString(3));  // Obtener el codigo de la materia.
                materia.setUv(rs.getInt(4));   // Obtener el uv de materia.
                materia.setId_docente(rs.getInt(5));//Obtener el Id del docente que imparte la materia
                // Agregar el objeto materia a la lista de resultados.
                records.add(materia);
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar materias: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return records; // Retornar la lista de usuarios encontrados.
    }


    public Materia getById(int id) throws SQLException{
        Materia materia  = new Materia(); // Inicializar un objeto Materia que se retornará.

        try {
            // Preparar la sentencia SQL para seleccionar un docente por su ID.
            ps = conn.connect().prepareStatement("SELECT id_materia, nombre_materia, codigo_materia, uv, id_docente " +
                    "FROM materias " +
                    "WHERE id_materia = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, id);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                // Si se encontró un usuario, asignar los valores de las columnas al objeto User.
                materia.setId_materia(rs.getInt(1));       // Obtener el ID del docente.
                materia.setNombre_materia(rs.getString(2));   // Obtener el id del usuario.
                materia.setCodigo_materia(rs.getString(3));  // Obtener el nombre del docente.
                materia.setUv(rs.getInt(4));    // Obtener el titulo del docente.
                materia.setId_docente(rs.getInt(5));//Obtener la experiencia del docente
            } else {
                // Si no se encontró ningún usuario con el ID especificado, establecer el objeto User a null.
                materia = null;
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al obtener un usuario por id: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return materia; // Retornar el objeto User encontrado o null si no existe.
    }

}
