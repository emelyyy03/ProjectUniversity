package com.practicasesfe.Persistencia;

import java.sql.Connection; // Representa una conexión a la base de datos.
import java.sql.DriverManager; // Gestiona los drivers JDBC y establece conexiones.
import java.sql.SQLException; // Representa errores específicos de la base de datos.

public class ConnectionManager {

    //Cadena de conexion
    private static final String STR_CONNECTION = "jdbc:sqlserver://DESKTOP-5PGNA2S\\SQLEXPRESS; " +
            "encrypt=true; " +
            "database=BDUniversidad; " +
            "trustServerCertificate=true;" +
            "user=java2025;" +
            "password=12345";

    private Connection connection;

    private static ConnectionManager instance;


    //Metodo para abrir la conexion
    private ConnectionManager() {
        this.connection = null;
        try {
            // mediante el driver se comunica con la base de datos SQL Server.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // Si el driver no se encuentra, se lanza una excepción indicando el error.
            throw new RuntimeException("Error al cargar el driver JDBC de SQL Server", e);
        }
    }


    public synchronized Connection connect() throws SQLException {
        // Verifica si la conexión ya existe y si no está cerrada.
        if (this.connection == null || this.connection.isClosed()) {
            try {
                // Intenta establecer la conexión utilizando la cadena de conexión.
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                // Si ocurre un error durante la conexión, se lanza una excepción SQLException
                throw new SQLException("Error al conectar a la base de datos: " + exception.getMessage(), exception);
            }
        }
        // Retorna la conexión (ya sea la existente o la recién creada).
        return this.connection;
    }


    //Metodo para cerrar la conexion
    public void disconnect() throws SQLException {
        // Verifica si la conexión existe.si no es nula.
        if (this.connection != null) {
            try {
                // Intenta cerrar la conexión.
                this.connection.close();
            } catch (SQLException exception) {
                // Si ocurre un error al cerrar la conexión, se lanza una excepción SQLException.
                throw new SQLException("Error al cerrar la conexión: " + exception.getMessage(), exception);
            } finally {
                // indica que ya no hay una conexión activa gestionada por esta instancia.
                this.connection = null;
            }
        }
    }

    //Metodo para hacer la instancia
    public static synchronized ConnectionManager getInstance() {
        // Verifica si la instancia ya ha sido creada.
        if (instance == null) {
            // Si no existe, crea una nueva instancia de JDBCConnectionManager.
            instance = new ConnectionManager();
        }
        // Retorna la instancia existente (o la recién creada).
        return instance;
    }
}
