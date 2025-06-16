package com.practicasesfe.Formularios;

import com.practicasesfe.Persistencia.HorarioDAO;
import com.practicasesfe.dominio.Horario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Horarios extends JPanel {
    // Componentes del formulario
    private JPanel rootPanel;
    private JTable table1;
    private JTextField textField1;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JTextField textField2;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JButton nuevoButton;
    private JButton editarButton;
    private JButton verButton;
    private JButton eliminarButton;

    // Conexion con la base de datos
    private HorarioDAO horarioDAO;
    //Propiedades
    private Horario horarioActual = null;
    private boolean modoEditar = false;

    public Horarios() {
        this.horarioDAO = new HorarioDAO();
        initSpinners();
        cargarHorarios();

        // Asignamos los eventos a los botones
        guardarButton.addActionListener(e -> guardarHorario());
        cancelarButton.addActionListener(e -> limpiarFormulario());
        buscarButton.addActionListener(e -> buscarHorarios());
        nuevoButton.addActionListener(e -> modoNuevo());
        editarButton.addActionListener(e -> seleccionarParaEditar());
        verButton.addActionListener(e -> seleccionarParaVer());
        eliminarButton.addActionListener(e -> eliminarHorario());

    }

    //Conexion con el formulario principal
    public JPanel getRootPanel() {
        return rootPanel;
    }

    //Configuracion de los spinner para que sean formato de hora
    private void initSpinners() {
        spinner1.setModel(new SpinnerDateModel());
        spinner2.setModel(new SpinnerDateModel());

        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spinner1, "HH:mm");
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinner2, "HH:mm");
        spinner1.setEditor(editor1);
        spinner2.setEditor(editor2);
    }

    //Muestra los horarios de la base de datos en la tabla
    private void cargarHorarios() {
        try {
            ArrayList<Horario> lista = horarioDAO.search("");
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Inicio", "Fin", "Días"}, 0);

            for (Horario h : lista) {
                model.addRow(new Object[]{
                        h.getIdHorario(),
                        h.getNombreHorario(),
                        h.getHoraInicio(),
                        h.getHoraFin(),
                        h.getDias()
                });
            }

            table1.setModel(model);
        } catch (SQLException ex) {
            mostrarError("Error al cargar horarios: " + ex.getMessage());
        }
    }

    //Desde aqui empezamos con los metodos de uso
    private void guardarHorario() {
        String nombre = textField1.getText().trim();
        String dias = textField2.getText().trim();
        LocalTime horaInicio = getSpinnerTime(spinner1);
        LocalTime horaFin = getSpinnerTime(spinner2);

        if (nombre.isEmpty() || dias.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        try {
            if (modoEditar && horarioActual != null) {
                horarioActual.setNombreHorario(nombre);
                horarioActual.setHoraInicio(horaInicio);
                horarioActual.setHoraFin(horaFin);
                horarioActual.setDias(dias);
                horarioDAO.update(horarioActual);
                mostrarMensaje("Horario actualizado exitosamente.");
            } else {
                Horario nuevo = new Horario(nombre, horaInicio, horaFin, dias);
                horarioDAO.create(nuevo);
                mostrarMensaje("Horario creado exitosamente.");
            }

            limpiarFormulario();
            cargarHorarios();
        } catch (SQLException ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private void buscarHorarios() {
        String term = textField1.getText().trim();
        try {
            ArrayList<Horario> lista = horarioDAO.search(term);
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Inicio", "Fin", "Días"}, 0);

            for (Horario h : lista) {
                model.addRow(new Object[]{
                        h.getIdHorario(),
                        h.getNombreHorario(),
                        h.getHoraInicio(),
                        h.getHoraFin(),
                        h.getDias()
                });
            }

            table1.setModel(model);
        } catch (SQLException ex) {
            mostrarError("Error al buscar: " + ex.getMessage());
        }
    }

    private void seleccionarParaEditar() {
        int row = table1.getSelectedRow();
        if (row >= 0) {
            horarioActual = obtenerHorarioDeTabla(row);
            llenarFormulario(horarioActual);
            modoEditar = true;
        } else {
            mostrarError("Selecciona un horario de la tabla.");
        }
    }

    private void seleccionarParaVer() {
        int row = table1.getSelectedRow();
        if (row >= 0) {
            Horario seleccionado = obtenerHorarioDeTabla(row);
            llenarFormulario(seleccionado);
            deshabilitarCampos();
        } else {
            mostrarError("Selecciona un horario de la tabla.");
        }
    }

    private void eliminarHorario() {
        int row = table1.getSelectedRow();
        if (row >= 0) {
            Horario seleccionado = obtenerHorarioDeTabla(row);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el horario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    horarioDAO.delete(seleccionado);
                    mostrarMensaje("Horario eliminado.");
                    cargarHorarios();
                    limpiarFormulario();
                } catch (SQLException ex) {
                    mostrarError("Error al eliminar: " + ex.getMessage());
                }
            }
        } else {
            mostrarError("Selecciona un horario para eliminar.");
        }
    }

    private void modoNuevo() {
        limpiarFormulario();
        habilitarCampos();
        modoEditar = false;
        horarioActual = null;
    }

    private void limpiarFormulario() {
        textField1.setText("");
        textField2.setText("");
        spinner1.setValue(new java.util.Date());
        spinner2.setValue(new java.util.Date());
        habilitarCampos();
        horarioActual = null;
        modoEditar = false;
    }

    private Horario obtenerHorarioDeTabla(int row) {
        int id = (int) table1.getValueAt(row, 0);
        try {
            return horarioDAO.getById(id);
        } catch (SQLException e) {
            mostrarError("Error al obtener el horario seleccionado.");
            return null;
        }
    }

    private void llenarFormulario(Horario h) {
        textField1.setText(h.getNombreHorario());
        textField2.setText(h.getDias());
        spinner1.setValue(java.sql.Time.valueOf(h.getHoraInicio()));
        spinner2.setValue(java.sql.Time.valueOf(h.getHoraFin()));
    }

    private void deshabilitarCampos() {
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        spinner1.setEnabled(false);
        spinner2.setEnabled(false);
        guardarButton.setEnabled(false);
    }

    private void habilitarCampos() {
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        spinner1.setEnabled(true);
        spinner2.setEnabled(true);
        guardarButton.setEnabled(true);
    }

    private LocalTime getSpinnerTime(JSpinner spinner) {
        java.util.Date date = (java.util.Date) spinner.getValue();
        return LocalTime.of(date.getHours(), date.getMinutes());
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }



}
