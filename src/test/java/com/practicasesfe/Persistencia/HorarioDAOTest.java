package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Horario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HorarioDAOTest {
    //Instancia
    private HorarioDAO horarioDAO;

    @BeforeEach
    void setUp() {
        horarioDAO = new HorarioDAO();
    }

    //Metodos
    private Horario create(Horario horario) throws SQLException {
        Horario res = horarioDAO.create(horario);
        assertNotNull(res, "El horario creado no debería ser nulo.");
        assertEquals(horario.getNombreHorario(), res.getNombreHorario(), "El nombre del horario debe coincidir.");
        assertEquals(horario.getHoraInicio(), res.getHoraInicio(), "La hora de inicio debe coincidir.");
        assertEquals(horario.getHoraFin(), res.getHoraFin(), "La hora de fin debe coincidir.");
        assertEquals(horario.getDias(), res.getDias(), "Los días deben coincidir.");
        return res;
    }

    private void update(Horario horario) throws SQLException {
        // Actualizar valores
        horario.setNombreHorario(horario.getNombreHorario() + " Actualizado");
        horario.setHoraInicio(horario.getHoraInicio().plusHours(1));
        horario.setHoraFin(horario.getHoraFin().plusHours(1));
        horario.setDias("Martes a Sábado");

        // Ejecutar actualización en la base de datos
        boolean res = horarioDAO.update(horario);
        assertTrue(res, "La actualización del horario debería ser exitosa.");

        // Validar que se actualizó correctamente
        getById(horario);
    }

    private void getById(Horario horario) throws SQLException {
        Horario res = horarioDAO.getById(horario.getIdHorario());
        assertNotNull(res, "El horario obtenido no debería ser nulo.");
        assertEquals(horario.getIdHorario(), res.getIdHorario(), "El ID del horario debe coincidir.");
        assertEquals(horario.getNombreHorario(), res.getNombreHorario(), "El nombre debe coincidir.");
        assertEquals(horario.getHoraInicio(), res.getHoraInicio(), "La hora de inicio debe coincidir.");
        assertEquals(horario.getHoraFin(), res.getHoraFin(), "La hora de fin debe coincidir.");
        assertEquals(horario.getDias(), res.getDias(), "Los días deben coincidir.");
    }


    private void search(Horario horario) throws SQLException {
        ArrayList<Horario> horarios = horarioDAO.search(horario.getNombreHorario());
        assertFalse(horarios.isEmpty(), "La búsqueda debería devolver al menos un horario.");
        boolean encontrado = horarios.stream()
                .allMatch(h -> h.getNombreHorario().contains(horario.getNombreHorario()));
        assertTrue(encontrado, "El nombre buscado no fue encontrado: " + horario.getNombreHorario());
    }

    private void delete(Horario horario) throws SQLException {
        boolean res = horarioDAO.delete(horario);
        assertTrue(res, "La eliminación del horario debería ser exitosa.");
        Horario res2 = horarioDAO.getById(horario.getIdHorario());
        assertNull(res2, "El horario debería haber sido eliminado y no encontrado por ID.");
    }

    //PRUEBAS

    @Test
    void testHorarioDAO() throws SQLException {
        // Se crea un nombre único para evitar duplicados en la BD
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String nombre = "HorarioT " + num;

        // Se crea un nuevo objeto Horario
        Horario horario = new Horario(
                0,
                nombre,
                LocalTime.of(1, 0),
                LocalTime.of(6, 0),
                "Miércoles, Viernes"
        );

        // Se llama a create (inserta en SQL Server y lo devuelve con ID generado)
        Horario testHorario = create(horario);

        // Luego llama a los demás métodos para probarlos
        update(testHorario);
        search(testHorario);
        delete(testHorario);
    }

    @Test
    void create() throws SQLException{
        Horario horario = new Horario(
                0,
                "Nocturno",
                LocalTime.of(7, 0),
                LocalTime.of(9, 0),
                "Martes, Miércoles y Jueves"
        );
        Horario res = horarioDAO.create(horario);
        assertNotNull(res);
    }
}