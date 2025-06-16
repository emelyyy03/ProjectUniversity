package com.practicasesfe.Formularios;

import com.practicasesfe.Persistencia.EstudianteHorarioDAO;
import com.practicasesfe.Persistencia.EstudiantesDAO;
import com.practicasesfe.Persistencia.HorarioDAO;
import com.practicasesfe.dominio.EstudianteHorario;
import com.practicasesfe.dominio.Estudiantes;
import com.practicasesfe.dominio.Horario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EstudianteHorarioForm extends JPanel {
    private JPanel rootPanel;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton asignarHorarioButton;
    private JTable table2;
    private JButton eliminarAsignacionButton;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private EstudiantesDAO estudiantesDAO = new EstudiantesDAO();
    private HorarioDAO horarioDAO = new HorarioDAO();
    private EstudianteHorarioDAO estudianteHorarioDAO = new EstudianteHorarioDAO();

    public EstudianteHorarioForm() {
        loadEstudiantes();
        loadHorarios();

        // Evento asignar
        asignarHorarioButton.addActionListener(e -> asignarHorario());

        // Evento al seleccionar estudiante
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarHorariosAsignados();
            }
        });

        // Evento eliminar asignación
        eliminarAsignacionButton.addActionListener(e -> eliminarAsignacion());
    }

    private void loadEstudiantes() {
        try {
            ArrayList<Estudiantes> lista = estudiantesDAO.search("");
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Carnet", "Nombre"}, 0);
            for (Estudiantes e : lista) {
                model.addRow(new Object[]{e.getIdEstudiante(), e.getCarnet(), e.getNombreCompleto()});
            }
            table1.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + ex.getMessage());
        }
    }

    private void loadHorarios() {
        try {
            comboBox1.removeAllItems();
            ArrayList<Horario> horarios = horarioDAO.search("");
            for (Horario h : horarios) {
                comboBox1.addItem(h);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage());
        }
    }

    private void asignarHorario() {
        int row = table1.getSelectedRow();
        Horario horarioSeleccionado = (Horario) comboBox1.getSelectedItem();

        if (row == -1 || horarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante y un horario");
            return;
        }

        int idEstudiante = (int) table1.getValueAt(row, 0);
        EstudianteHorario eh = new EstudianteHorario(idEstudiante, horarioSeleccionado.getIdHorario());

        try {
            estudianteHorarioDAO.asignarHorario(eh);
            JOptionPane.showMessageDialog(this, "Horario asignado correctamente");
            mostrarHorariosAsignados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al asignar: " + ex.getMessage());
        }
    }

    private void mostrarHorariosAsignados() {
        int row = table1.getSelectedRow();
        if (row == -1) return;

        int idEstudiante = (int) table1.getValueAt(row, 0);

        try {
            ArrayList<Horario> lista = estudianteHorarioDAO.getHorariosByEstudiante(idEstudiante);
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Inicio", "Fin", "Días"}, 0);
            for (Horario h : lista) {
                model.addRow(new Object[]{
                        h.getIdHorario(), h.getNombreHorario(),
                        h.getHoraInicio(), h.getHoraFin(), h.getDias()
                });
            }
            table2.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener horarios: " + ex.getMessage());
        }
    }

    private void eliminarAsignacion() {
        int rowEstudiante = table1.getSelectedRow();
        int rowHorario = table2.getSelectedRow();

        if (rowEstudiante == -1 || rowHorario == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione estudiante y horario a eliminar");
            return;
        }

        int idEstudiante = (int) table1.getValueAt(rowEstudiante, 0);
        int idHorario = (int) table2.getValueAt(rowHorario, 0);
        EstudianteHorario eh = new EstudianteHorario(idEstudiante, idHorario);

        try {
            estudianteHorarioDAO.delete(eh);
            JOptionPane.showMessageDialog(this, "Asignación eliminada");
            mostrarHorariosAsignados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}
