package com.practicasesfe.Persistencia;

import com.practicasesfe.dominio.Estudiantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EstudiantesDAOTest {

    private EstudiantesDAO estudiantesDAO;

    @BeforeEach
    void setUp(){
        estudiantesDAO = new EstudiantesDAO();
    }

    private Estudiantes create(Estudiantes estudiante) throws SQLException {
        Estudiantes res = estudiantesDAO.create(estudiante);
        assertNotNull(res, "El estudiante creado no debería ser nulo.");
        assertEquals(estudiante.getCarnet(), res.getCarnet(), "El carnet debe coincidir.");
        assertEquals(estudiante.getNombreCompleto(), res.getNombreCompleto(), "El nombre completo debe coincidir.");
        assertEquals(estudiante.getCarrera(), res.getCarrera(), "La carrera debe coincidir.");
        assertEquals(estudiante.getFechaIngreso(), res.getFechaIngreso(), "La fecha de ingreso debe coincidir.");
        assertEquals(estudiante.getPromedioNotas(), res.getPromedioNotas(), "El promedio debe coincidir.");
        assertEquals(estudiante.getModalidadEstudio(), res.getModalidadEstudio(), "La modalidad debe coincidir.");
        assertEquals(estudiante.getIdUsuario(), res.getIdUsuario(), "El ID de usuario debe coincidir.");
        return res;
    }

    private void update(Estudiantes estudiante) throws SQLException {
        // Actualizar algunos campos
        estudiante.setNombreCompleto(estudiante.getNombreCompleto() + " Actualizado");
        estudiante.setCarrera(estudiante.getCarrera() + " Mod");
        estudiante.setPromedioNotas(estudiante.getPromedioNotas().add(new BigDecimal("0.50")));
        estudiante.setFechaIngreso(estudiante.getFechaIngreso().plusDays(1));
        estudiante.setModalidadEstudio("Virtual");

        boolean res = estudiantesDAO.update(estudiante);
        assertTrue(res, "La actualización del estudiante debería ser exitosa.");

        getById(estudiante);
    }

    private void getById(Estudiantes estudiante) throws SQLException {
        Estudiantes res = estudiantesDAO.getById(estudiante.getIdEstudiante());
        assertNotNull(res, "El estudiante obtenido no debería ser nulo.");
        assertEquals(estudiante.getIdEstudiante(), res.getIdEstudiante(), "El ID debe coincidir.");
        assertEquals(estudiante.getCarnet(), res.getCarnet(), "El carnet debe coincidir.");
        assertEquals(estudiante.getNombreCompleto(), res.getNombreCompleto(), "El nombre completo debe coincidir.");
        assertEquals(estudiante.getCarrera(), res.getCarrera(), "La carrera debe coincidir.");
        assertEquals(estudiante.getFechaIngreso(), res.getFechaIngreso(), "La fecha de ingreso debe coincidir.");
        assertEquals(estudiante.getPromedioNotas(), res.getPromedioNotas(), "El promedio debe coincidir.");
        assertEquals(estudiante.getModalidadEstudio(), res.getModalidadEstudio(), "La modalidad debe coincidir.");
        assertEquals(estudiante.getIdUsuario(), res.getIdUsuario(), "El ID de usuario debe coincidir.");
    }

    private void search(Estudiantes estudiante) throws SQLException {
        ArrayList<Estudiantes> estudiantes = estudiantesDAO.search(estudiante.getNombreCompleto().split(" ")[0]);
        assertFalse(estudiantes.isEmpty(), "La búsqueda debería devolver al menos un estudiante.");
        boolean encontrado = estudiantes.stream()
                .anyMatch(e -> e.getNombreCompleto().contains(estudiante.getNombreCompleto().split(" ")[0]));
        assertTrue(encontrado, "El nombre buscado no fue encontrado: " + estudiante.getNombreCompleto());
    }

    private void delete(Estudiantes estudiante) throws SQLException {
        boolean res = estudiantesDAO.delete(estudiante);
        assertTrue(res, "La eliminación del estudiante debería ser exitosa.");
        Estudiantes res2 = estudiantesDAO.getById(estudiante.getIdEstudiante());
        assertNull(res2, "El estudiante debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testEstudianteDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String carnet = "ESF3" + num;

        Estudiantes estudiante = new Estudiantes(
                0,
                carnet,
                "Nayeli Arriola " + num,
                "Derecho",
                LocalDateTime.of(2025, 4, 26, 8, 0),
                new BigDecimal("10.00"),
                "Virtual",
                null // o puedes poner un ID válido si lo tienes
        );

        Estudiantes testEstudiante = create(estudiante);
        update(testEstudiante);
        search(testEstudiante);
        delete(testEstudiante);
    }


    @Test
    void create() throws SQLException{
        Estudiantes estudiante = new Estudiantes(
                0,
                "ESF04",
                "Andrea Miranda",
                "Diseño Grafico",
                LocalDateTime.of(2025, 7, 8, 7, 30),
                new BigDecimal("9.00"),
                "Presencial",
                1
        );
        Estudiantes res = estudiantesDAO.create(estudiante);
        assertNotNull(res);
    }
}