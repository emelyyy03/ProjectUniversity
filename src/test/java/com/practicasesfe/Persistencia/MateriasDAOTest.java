package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Docente;
import com.practicasesfe.dominio.Materia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MateriasDAOTest {

    private MateriasDAO materiasDAO; // Instancia de la clase UserDAO que se va a probar.

    @BeforeEach
    void setUp(){
        // Método que se ejecuta antes de cada método de prueba (@Test).
        // Su propósito es inicializar el entorno de prueba, en este caso,
        // creando una nueva instancia de UserDAO para cada prueba.
        materiasDAO = new MateriasDAO();
    }
    private Materia create(Materia materia) throws SQLException {
        // Llama al método 'create' del UserDAO para persistir el usuario en la base de datos (simulada).
        Materia res = materiasDAO.create(materia);

        // Realiza aserciones para verificar que la creación del usuario fue exitosa
        // y que los datos del usuario retornado coinciden con los datos originales.
        assertNotNull(res, "La materia creada no debería ser nula.");
        assertEquals(materia.getNombre_materia(), res.getNombre_materia(), "El nombre de la materia creada debe ser igual al original.");
        assertEquals(materia.getCodigo_materia(), res.getCodigo_materia(), "El código de la materia creada debe ser igual al original.");
        assertEquals(materia.getUv(), res.getUv(), "El UV de la materia debe ser igual al original.");
        assertEquals(materia.getId_docente(), res.getId_docente(), "El ID del docente debe ser igual al original.");

        // Retorna el objeto User creado (tal como lo devolvió el UserDAO).
        return res;
    }

    private void update(Materia materia) throws SQLException{
        // Modifica los atributos del objeto Docente para simular una actualización.
        materia.setId_materia(materia.getId_materia());
        materia.setNombre_materia(materia.getNombre_materia() + "_u"); // Añade "_u" al final del nombre.
        materia.setCodigo_materia("u" + materia.getCodigo_materia()); // Añade "u" al inicio del codigo.
        materia.setUv(materia.getUv());
        materia.setId_docente(materia.getId_docente());

        // Llama al método 'update' del UserDAO para actualizar el usuario en la base de datos (simulada).
        boolean res = materiasDAO.update(materia);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización del docente debería ser exitosa.");

        // Llama al método 'getById' para verificar que los cambios se persistieron correctamente.
        // Aunque el método 'getById' ya tiene sus propias aserciones, esta llamada adicional
        // ayuda a asegurar que la actualización realmente tuvo efecto en la capa de datos.
        getById(materia);
    }

    private void getById(Materia materia) throws SQLException {
        // Llama al método 'getById' del UserDAO para obtener un usuario por su ID.
        Materia res = materiasDAO.getById(materia.getId_materia());

        // Realiza aserciones para verificar que el usuario obtenido coincide
        // con el usuario original (o el usuario modificado en pruebas de actualización).
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(materia.getId_materia(), res.getId_materia(), "El ID de materia debe ser igual al original.");
        assertEquals(materia.getNombre_materia(), res.getNombre_materia(), "El id de materia debe ser igual al esperado.");
        assertEquals(materia.getCodigo_materia(), res.getCodigo_materia(), "El codio de materia debe ser igual al esperado.");
        assertEquals(materia.getUv(), res.getUv(), "El uv de materia debe ser igual al esperado.");
        assertEquals(materia.getId_docente(), res.getId_docente(), "El id de docente debe ser igual al resultado esperado");
    }

    private void search(Materia materia) throws SQLException {
        // Llama al método 'search' del MateriasDAO para buscar materias por nombre.
        ArrayList<Materia> materias = materiasDAO.search(materia.getNombre_materia());
        boolean find = false; // Variable para rastrear si se encontró una materia con el nombre buscado.

        // Itera sobre la lista de usuarios devuelta por la búsqueda.
        for (Materia materiaItem : materias) {
            // Verifica si el nombre de cada usuario encontrado contiene la cadena de búsqueda.
            if (materiaItem.getNombre_materia().contains(materia.getNombre_materia())) {
                find = true; // Si se encuentra una coincidencia, se establece 'find' a true.
            }
            else{
                find = false; // Si un nombre no contiene la cadena de búsqueda, se establece 'find' a false.
                break;      // Se sale del bucle, ya que se esperaba que todos los resultados contuvieran la cadena.
            }
        }

        // Realiza una aserción para verificar que todos los usuarios con el nombre buscado fue encontrado.
        assertTrue(find, "el nombre buscado no fue encontrado : " + materia.getNombre_materia());
    }

    private void delete(Materia materia) throws SQLException{
        // Llama al método 'delete' del MateriasDAO para eliminar una materia por su ID.
        boolean res = materiasDAO.delete(materia);

        // Realiza una aserción para verificar que la eliminación fue exitosa.
        assertTrue(res, "La eliminación de la materia debería ser exitosa.");

        // Intenta obtener el usuario por su ID después de la eliminación.
        Materia res2 = materiasDAO.getById(materia.getId_materia());

        // Realiza una aserción para verificar que el usuario ya no existe en la base de datos
        // después de la eliminación (el método 'getById' debería retornar null).
        assertNull(res2, "La materia debería haber sido eliminada y no encontrado por ID.");
    }

    @Test
    void testMateriaDAO() throws SQLException {
        // Generar un número aleatorio para asegurar unicidad
        Random random = new Random();
        int num = random.nextInt(1000) + 1;

        // Crear una nueva materia con datos de prueba (ID en 0 porque se genera automáticamente)
        Materia materia = new Materia(0, "Ciencias " + num, "Ciencia" + num, 8, 3);

        // Crear la materia en la base de datos
        Materia testMateria = create(materia);

        // Reemplazamos la comparación de ID en create() (ya que testMateria ahora tiene un ID nuevo)
        // Por eso usamos testMateria en todos los pasos posteriores

        // Probar la actualización
        update(testMateria);

        // Probar la búsqueda
        search(testMateria);

        // Probar la eliminación
        delete(testMateria);
    }

    @Test
    void createMateria() throws SQLException {
        Materia materia = new Materia(0,"Ciencias", "Cienc08", 8, 5);
        Materia res = materiasDAO.create(materia);
        assertNotEquals(res,null);
    }

}