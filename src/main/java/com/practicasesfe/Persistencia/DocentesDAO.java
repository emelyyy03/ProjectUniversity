package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Docente;
import com.practicasesfe.dominio.User;
import com.practicasesfe.utils.PasswordHasher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocentesDAO {

    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    public DocentesDAO(){
        conn = ConnectionManager.getInstance();
    }

    public Docente create(Docente docente) throws SQLException {
        Docente res = null; // Variable para almacenar el usuario creado que se retornará.
        try{
            // Preparar la sentencia SQL para la inserción de un nuevo usuario.
            // Se especifica que se retornen las claves generadas automáticamente.
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "docentes (id_usuario, nombre_completo, titulo, experiencia_anios, fecha_contratacion) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setInt(1, docente.getId_usuario()); // Asignar el id del usuario.
            ps.setString(2, docente.getNombre_completo()); // Asignar el nombre del docente.
            ps.setString(3, docente.getTitulo()); //Titulo del docente
            ps.setDouble(4, docente.getExperiencia_anios()); //Asigna los años de experiencia del docente
            ps.setDate(5, docente.getFecha_contratacion()); //Asigna fecha de contratacion del docente


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
        return res; // Retornar el usuario creado (con su ID asignado) o null si hubo un error.
    }


    public boolean update(Docente docente) throws SQLException{
        boolean res = false; // Variable para indicar si la actualización fue exitosa.
        try{
            // Preparar la sentencia SQL para actualizar la información de un usuario.
            ps = conn.connect().prepareStatement(
                    "UPDATE docentes " +
                            "SET id_usuario = ?, nombre_completo = ?, titulo = ?, experiencia_anios = ? " +
                            "WHERE id_docente = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setInt(1, docente.getId_usuario());  // Asignar el nuevo id del usuario.
            ps.setString(2, docente.getNombre_completo()); // Asignar el nuevo nombre del docente.
            ps.setString(3, docente.getTitulo()); //Asigna el nuevo titulo del docente
            ps.setDouble(4, docente.getExperiencia_anios()); //Asignar nueva cantidad de años de experiencia
            ps.setInt(5, docente.getId_docente());       // Establecer la condición WHERE para identificar el docente a actualizar por su ID.

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


    public boolean delete(Docente docente) throws SQLException{
        boolean res = false; // Variable para indicar si la eliminación fue exitosa.
        try{
            // Preparar la sentencia SQL para eliminar un usuario por su ID.
            ps = conn.connect().prepareStatement(
                    "DELETE FROM docentes WHERE id_docente = ?"
            );
            // Establecer el valor del parámetro en la sentencia preparada (el ID del usuario a eliminar).
            ps.setInt(1, docente.getId_docente());

            // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la eliminación fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de eliminación.
    }


    public ArrayList<Docente> search(String name) throws SQLException{
        ArrayList<Docente> records  = new ArrayList<>(); // Lista para almacenar los docentes encontrados.

        try {
            // Preparar la sentencia SQL para buscar usuarios por nombre (usando LIKE para búsqueda parcial).
            ps = conn.connect().prepareStatement("SELECT id_docente, nombre_completo, titulo, experiencia_anios, fecha_contratacion " +
                    "FROM docentes " +
                    "WHERE nombre_completo LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'name' en cualquier parte del nombre del usuario.
            ps.setString(1, "%" + name + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()){
                // Crear un nuevo objeto User para cada registro encontrado.
                Docente docente = new Docente();
                // Asignar los valores de las columnas a los atributos del objeto User.
                docente.setId_docente(rs.getInt(1));       // Obtener el ID del docente.
                docente.setNombre_completo(rs.getString(2));   // Obtener el nombre del docente.
                docente.setTitulo(rs.getString(3));  // Obtener el titulo del docente.
                docente.setExperiencia_anios(rs.getDouble(4));   // Obtener la experiencia del docente.
                docente.setFecha_contratacion(rs.getDate(5));//Obtener la fecha de contratacion del docente
                // Agregar el objeto docente a la lista de resultados.
                records.add(docente);
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar docentes: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return records; // Retornar la lista de usuarios encontrados.
    }


    public Docente getById(int id) throws SQLException{
        Docente docente  = new Docente(); // Inicializar un objeto Docente que se retornará.

        try {
            // Preparar la sentencia SQL para seleccionar un docente por su ID.
            ps = conn.connect().prepareStatement("SELECT id_docente, id_usuario, nombre_completo, titulo, experiencia_anios, fecha_contratacion " +
                    "FROM docentes " +
                    "WHERE id_docente = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, id);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                // Si se encontró un usuario, asignar los valores de las columnas al objeto User.
                docente.setId_docente(rs.getInt(1));       // Obtener el ID del docente.
                docente.setId_usuario(rs.getInt(2));   // Obtener el id del usuario.
                docente.setNombre_completo(rs.getString(3));  // Obtener el nombre del docente.
                docente.setTitulo(rs.getString(4));    // Obtener el titulo del docente.
                docente.setExperiencia_anios(rs.getDouble(5));//Obtener la experiencia del docente
                docente.setFecha_contratacion(rs.getDate(6)); //Obtener la fecha de contratacion del docente
            } else {
                // Si no se encontró ningún usuario con el ID especificado, establecer el objeto User a null.
                docente = null;
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
        return docente; // Retornar el objeto User encontrado o null si no existe.
    }

}
