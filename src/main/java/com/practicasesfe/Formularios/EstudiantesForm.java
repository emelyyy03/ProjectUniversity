package com.practicasesfe.Formularios;

import com.practicasesfe.Persistencia.EstudiantesDAO;
import com.practicasesfe.dominio.Estudiantes;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EstudiantesForm extends JPanel {
    private JPanel rootPanel;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JFormattedTextField formattedTextField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JSpinner spinner1;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JButton crearButton;
    private JButton eliminarButton;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private DefaultTableModel tableModel;
    private EstudiantesDAO estudiantesDAO;
    private Estudiantes estudianteSeleccionado;

    public EstudiantesForm() {
        estudiantesDAO = new EstudiantesDAO();
        configurarTabla();
        cargarComboBoxModalidad();
        cargarComboBoxUsuarios();
        configurarSpinnerFecha(); // <-- Agregamos la configuración aquí
        cargarEstudiantes();

        guardarButton.addActionListener(e -> guardarEstudiante());
        crearButton.addActionListener(e -> limpiarCampos());
        eliminarButton.addActionListener(e -> eliminarEstudiante());
        buscarButton.addActionListener(e -> buscarEstudiante());
        cancelarButton.addActionListener(e -> limpiarCampos());

        table1.getSelectionModel().addListSelectionListener(e -> seleccionarEstudiante());
    }

    private void configurarTabla() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Carnet", "Nombre", "Carrera", "Ingreso", "Promedio", "Modalidad", "ID Usuario"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(tableModel);
    }

    private void cargarComboBoxModalidad() {
        comboBox1.removeAllItems();
        comboBox1.addItem("Presencial");
        comboBox1.addItem("Virtual");
        comboBox1.addItem("Semipresencial");
    }

    private void cargarComboBoxUsuarios() {
        comboBox2.removeAllItems();
        try {
            List<Integer> ids = estudiantesDAO.getIdsUsuarios();
            for (Integer id : ids) {
                comboBox2.addItem(id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando usuarios: " + e.getMessage());
        }
    }

    private void cargarEstudiantes() {
        tableModel.setRowCount(0);
        try {
            List<Estudiantes> lista = estudiantesDAO.search("");
            for (Estudiantes est : lista) {
                tableModel.addRow(new Object[]{
                        est.getIdEstudiante(), est.getCarnet(), est.getNombreCompleto(),
                        est.getCarrera(), est.getFechaIngreso(), est.getPromedioNotas(),
                        est.getModalidadEstudio(), est.getIdUsuario()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + e.getMessage());
        }
    }

    private void guardarEstudiante() {
        try {
            String carnet = textField1.getText();
            String nombre = textField2.getText();
            String carrera = textField3.getText();
            LocalDateTime ingreso = convertirFecha((Date) spinner1.getValue());
            BigDecimal promedio = new BigDecimal(formattedTextField1.getText());
            String modalidad = (String) comboBox1.getSelectedItem();
            Integer idUsuario = (Integer) comboBox2.getSelectedItem();

            if (estudianteSeleccionado == null) {
                Estudiantes nuevo = new Estudiantes(0, carnet, nombre, carrera, ingreso, promedio, modalidad, idUsuario);
                estudiantesDAO.create(nuevo);
                JOptionPane.showMessageDialog(this, "Estudiante creado.");
            } else {
                estudianteSeleccionado.setCarnet(carnet);
                estudianteSeleccionado.setNombreCompleto(nombre);
                estudianteSeleccionado.setCarrera(carrera);
                estudianteSeleccionado.setFechaIngreso(ingreso);
                estudianteSeleccionado.setPromedioNotas(promedio);
                estudianteSeleccionado.setModalidadEstudio(modalidad);
                estudianteSeleccionado.setIdUsuario(idUsuario);
                estudiantesDAO.update(estudianteSeleccionado);
                JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
            }
            limpiarCampos();
            cargarEstudiantes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarEstudiante() {
        if (estudianteSeleccionado != null) {
            try {
                estudiantesDAO.delete(estudianteSeleccionado);
                JOptionPane.showMessageDialog(this, "Estudiante eliminado.");
                limpiarCampos();
                cargarEstudiantes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void buscarEstudiante() {
        String texto = textField2.getText();
        tableModel.setRowCount(0);
        try {
            List<Estudiantes> lista = estudiantesDAO.search(texto);
            for (Estudiantes est : lista) {
                tableModel.addRow(new Object[]{
                        est.getIdEstudiante(), est.getCarnet(), est.getNombreCompleto(),
                        est.getCarrera(), est.getFechaIngreso(), est.getPromedioNotas(),
                        est.getModalidadEstudio(), est.getIdUsuario()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage());
        }
    }

    private void seleccionarEstudiante() {
        int fila = table1.getSelectedRow();
        if (fila >= 0) {
            Integer id = (Integer) tableModel.getValueAt(fila, 0);
            try {
                estudianteSeleccionado = estudiantesDAO.getById(id);
                if (estudianteSeleccionado != null) {
                    textField1.setText(estudianteSeleccionado.getCarnet());
                    textField2.setText(estudianteSeleccionado.getNombreCompleto());
                    textField3.setText(estudianteSeleccionado.getCarrera());
                    formattedTextField1.setText(estudianteSeleccionado.getPromedioNotas().toString());
                    spinner1.setValue(Date.from(estudianteSeleccionado.getFechaIngreso().atZone(ZoneId.systemDefault()).toInstant()));
                    comboBox1.setSelectedItem(estudianteSeleccionado.getModalidadEstudio());
                    comboBox2.setSelectedItem(estudianteSeleccionado.getIdUsuario());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        estudianteSeleccionado = null;
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        formattedTextField1.setText("");
        spinner1.setValue(new Date());
        comboBox1.setSelectedIndex(0);
        if (comboBox2.getItemCount() > 0) comboBox2.setSelectedIndex(0);
        table1.clearSelection();
    }

    private LocalDateTime convertirFecha(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    //  Este método configura correctamente el modelo del JSpinner
    private void configurarSpinnerFecha() {
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinner1.setModel(dateModel);

        // Formato de fecha visible (editable en el formulario)
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner1, "yyyy-MM-dd HH:mm");
        spinner1.setEditor(editor);
    }

}
