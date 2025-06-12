package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Docente;
import com.practicasesfe.dominio.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DocentesDAOTest {

    private DocentesDAO docenteDAO; // Instancia de la clase UserDAO que se va a probar.

    @BeforeEach
    void setUp(){
        // Método que se ejecuta antes de cada método de prueba (@Test).
        // Su propósito es inicializar el entorno de prueba, en este caso,
        // creando una nueva instancia de UserDAO para cada prueba.
        docenteDAO = new DocentesDAO();
    }
    private Docente create(Docente docente) throws SQLException {
        // Llama al método 'create' del UserDAO para persistir el usuario en la base de datos (simulada).
        Docente res = docenteDAO.create(docente);

        // Realiza aserciones para verificar que la creación del usuario fue exitosa
        // y que los datos del usuario retornado coinciden con los datos originales.
        assertNotNull(res, "El docente creado no debería ser nulo."); // Verifica que el objeto retornado no sea nulo.
        assertEquals(docente.getId_usuario(), res.getId_usuario(), "El Id del usuario creado debe ser igual al original.");
        assertEquals(docente.getNombre_completo(), res.getNombre_completo(), "El nombre del docente creado debe ser igual al original.");
        assertEquals(docente.getTitulo(), res.getTitulo(), "El titulo del docente creado debe ser igual al original.");
        assertEquals(docente.getExperiencia_anios(), res.getExperiencia_anios(), "Los años de experiencia deben ser igual al original");
        assertEquals(docente.getFecha_contratacion(), res.getFecha_contratacion(), "La fecha de contratacion debe ser igual a la original");

        // Retorna el objeto User creado (tal como lo devolvió el UserDAO).
        return res;
    }

    private void update(Docente docente) throws SQLException{
        // Modifica los atributos del objeto Docente para simular una actualización.
        docente.setId_usuario(docente.getId_usuario());
        docente.setNombre_completo(docente.getNombre_completo() + "_u"); // Añade "_u" al final del nombre.
        docente.setTitulo("u" + docente.getTitulo()); // Añade "u" al inicio del titulo.
        docente.setExperiencia_anios(docente.getExperiencia_anios());
        docente.setFecha_contratacion(docente.getFecha_contratacion());

        // Llama al método 'update' del UserDAO para actualizar el usuario en la base de datos (simulada).
        boolean res = docenteDAO.update(docente);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización del docente debería ser exitosa.");

        // Llama al método 'getById' para verificar que los cambios se persistieron correctamente.
        // Aunque el método 'getById' ya tiene sus propias aserciones, esta llamada adicional
        // ayuda a asegurar que la actualización realmente tuvo efecto en la capa de datos.
        getById(docente);
    }

    private void getById(Docente docente) throws SQLException {
        // Llama al método 'getById' del UserDAO para obtener un usuario por su ID.
        Docente res = docenteDAO.getById(docente.getId_docente());

        // Realiza aserciones para verificar que el usuario obtenido coincide
        // con el usuario original (o el usuario modificado en pruebas de actualización).
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(docente.getId_docente(), res.getId_docente(), "El ID del docente obtenido debe ser igual al original.");
        assertEquals(docente.getId_usuario(), res.getId_usuario(), "El id del usuario obtenido debe ser igual al esperado.");
        assertEquals(docente.getNombre_completo(), res.getNombre_completo(), "El nomre del docente obtenido debe ser igual al esperado.");
        assertEquals(docente.getTitulo(), res.getTitulo(), "El titulo del docente obtenido debe ser igual al esperado.");
        assertEquals(docente.getExperiencia_anios(), res.getExperiencia_anios(), "La experiencia debe ser igual al resultado esperado");
        assertEquals(docente.getFecha_contratacion(), res.getFecha_contratacion(), "La fecha de contratacion debe ser igual al resultado esperado");
    }

    private void search(Docente docente) throws SQLException {
        // Llama al método 'search' del UserDAO para buscar usuarios por nombre.
        ArrayList<Docente> docentes = docenteDAO.search(docente.getNombre_completo());
        boolean find = false; // Variable para rastrear si se encontró un usuario con el nombre buscado.

        // Itera sobre la lista de usuarios devuelta por la búsqueda.
        for (Docente docenteItem : docentes) {
            // Verifica si el nombre de cada usuario encontrado contiene la cadena de búsqueda.
            if (docenteItem.getNombre_completo().contains(docente.getNombre_completo())) {
                find = true; // Si se encuentra una coincidencia, se establece 'find' a true.
            }
            else{
                find = false; // Si un nombre no contiene la cadena de búsqueda, se establece 'find' a false.
                break;      // Se sale del bucle, ya que se esperaba que todos los resultados contuvieran la cadena.
            }
        }

        // Realiza una aserción para verificar que todos los usuarios con el nombre buscado fue encontrado.
        assertTrue(find, "el nombre buscado no fue encontrado : " + docente.getNombre_completo());
    }

    private void delete(Docente docente) throws SQLException{
        // Llama al método 'delete' del UserDAO para eliminar un usuario por su ID.
        boolean res = docenteDAO.delete(docente);

        // Realiza una aserción para verificar que la eliminación fue exitosa.
        assertTrue(res, "La eliminación del docente debería ser exitosa.");

        // Intenta obtener el usuario por su ID después de la eliminación.
        Docente res2 = docenteDAO.getById(docente.getId_docente());

        // Realiza una aserción para verificar que el usuario ya no existe en la base de datos
        // después de la eliminación (el método 'getById' debería retornar null).
        assertNull(res2, "El docente debería haber sido eliminado y no encontrado por ID.");
    }

    Date fechaContratacion = Date.valueOf(LocalDate.of(2025, 6, 10));
    //Date fechaActualizacion = Date.valueOf(LocalDate.of(2025, 6, 10));
    @Test
    void testUserDAO() throws SQLException {
        // Crea una instancia de la clase Random para generar datos de prueba aleatorios.
        Random random = new Random();
        // Genera un número aleatorio entre 1 y 1000 para asegurar la unicidad del email en cada prueba.
        int num = random.nextInt(1000) + 1;
        // Define una cadena base para el email y le concatena el número aleatorio generado.
        String strEmail = "test" + num + "@example.com";
        // Crea un nuevo objeto User con datos de prueba. El ID se establece en 0 ya que será generado por la base de datos.
        Docente docente = new Docente(1, 2, "Test Docente", "Doctorado", 2.5, fechaContratacion);

        // Llama al método 'create' para persistir el usuario de prueba en la base de datos (simulada) y verifica su creación.
        Docente testDocente = create(docente);

        // Llama al método 'update' para modificar los datos del usuario de prueba y verifica la actualización.
        update(testDocente);

        // Llama al método 'search' para buscar usuarios por el nombre del usuario de prueba y verifica que se encuentre.
        search(testDocente);

        // Llama al método 'delete' para eliminar el usuario de prueba de la base de datos y verifica la eliminación.
        delete(testDocente);
    }
    @Test
    void createDocente() throws SQLException {
        Docente docente = new Docente(1,1, "Diana Marquez", "Maestria", 5.5, fechaContratacion);
        Docente res = docenteDAO.create(docente);
        assertNotEquals(res,null);
    }

}