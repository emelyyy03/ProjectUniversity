package com.practicasesfe.Formularios;

import com.practicasesfe.Persistencia.CicloDAO;
import com.practicasesfe.dominio.Ciclo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Ciclos extends JPanel {
    //Campos utilizados en el .form
    private JPanel rootPanel;
    private JTextField textField1;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JTable table1;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JButton crearButton;
    private JButton editarButton;
    private JButton verButton;
    private JButton eliminarButton;

    //Metodo para conectar con el formulario principal
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private CicloDAO cicloDAO;
    private Ciclo cicloActual;
    private boolean modoEditar = false;

    public Ciclos() {
        cicloDAO = new CicloDAO();

        // Configuración de los JSpinners para fechas
        spinner1.setModel(new SpinnerDateModel());
        spinner2.setModel(new SpinnerDateModel());
        spinner1.setEditor(new JSpinner.DateEditor(spinner1, "yyyy-MM-dd"));
        spinner2.setEditor(new JSpinner.DateEditor(spinner2, "yyyy-MM-dd"));

        configurarEventos();
        cargarCiclos();
    }

    //Maneja las aciones que se realizaran por el usuario
    private void configurarEventos() {
        crearButton.addActionListener(e -> limpiarFormulario());

        guardarButton.addActionListener(e -> guardarCiclo());

        editarButton.addActionListener(e -> {
            if (cicloActual != null) {
                modoEditar = true;
                llenarFormulario(cicloActual);
            }
        });

        verButton.addActionListener(e -> {
            if (cicloActual != null) {
                llenarFormulario(cicloActual);
                deshabilitarFormulario();
            }
        });

        eliminarButton.addActionListener(e -> eliminarCiclo());

        cancelarButton.addActionListener(e -> {
            limpiarFormulario();
            habilitarFormulario();
        });

        buscarButton.addActionListener(e -> buscarCiclos());


        // Maneja la selección de filas en la tabla
        table1.getSelectionModel().addListSelectionListener(e -> {
            int fila = table1.getSelectedRow();
            if (fila >= 0) {
                cicloActual = new Ciclo();
                cicloActual.setIdCiclo((int) table1.getValueAt(fila, 0));
                cicloActual.setNombreCiclo((String) table1.getValueAt(fila, 1));
                cicloActual.setFechaInicio(Date.valueOf((String) table1.getValueAt(fila, 2)));
                cicloActual.setFechaFin(Date.valueOf((String) table1.getValueAt(fila, 3)));
            }
        });
    }

    //Desde aqui empiezan los eventos de la funcionalidad del crud segun los registros de la tabla
    private void guardarCiclo() {
        String nombre = textField1.getText();
        Date fechaInicio = convertirFecha(spinner1.getValue());
        Date fechaFin = convertirFecha(spinner2.getValue());

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del ciclo");
            return;
        }

        if (modoEditar && cicloActual != null) {
            cicloActual.setNombreCiclo(nombre);
            cicloActual.setFechaInicio(fechaInicio);
            cicloActual.setFechaFin(fechaFin);
            try {
                if (cicloDAO.update(cicloActual)) {
                    JOptionPane.showMessageDialog(this, "Ciclo actualizado exitosamente");
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        } else {
            Ciclo nuevoCiclo = new Ciclo(0, nombre, fechaInicio, fechaFin);
            try {
                cicloDAO.create(nuevoCiclo);
                JOptionPane.showMessageDialog(this, "Ciclo creado exitosamente");
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }

        limpiarFormulario();
        cargarCiclos();
        habilitarFormulario();
        modoEditar = false;
    }

    private void eliminarCiclo() {
        if (cicloActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un ciclo para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este ciclo?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cicloDAO.delete(cicloActual);
                JOptionPane.showMessageDialog(this, "Ciclo eliminado exitosamente");
                limpiarFormulario();
                cargarCiclos();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }

    private void buscarCiclos() {
        String nombre = textField1.getText();
        try {
            List<Ciclo> lista = cicloDAO.search(nombre);
            cargarTabla(lista);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarCiclos() {
        try {
            List<Ciclo> lista = cicloDAO.search(""); // traer todos
            cargarTabla(lista);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarTabla(List<Ciclo> lista) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Fecha Inicio", "Fecha Fin"}, 0);

        for (Ciclo c : lista) {
            modelo.addRow(new Object[]{
                    c.getIdCiclo(),
                    c.getNombreCiclo(),
                    c.getFechaInicio().toString(),
                    c.getFechaFin().toString()
            });
        }

        table1.setModel(modelo);
    }

    private void llenarFormulario(Ciclo ciclo) {
        textField1.setText(ciclo.getNombreCiclo());
        spinner1.setValue(ciclo.getFechaInicio());
        spinner2.setValue(ciclo.getFechaFin());
    }

    private void limpiarFormulario() {
        textField1.setText("");
        spinner1.setValue(new java.util.Date());
        spinner2.setValue(new java.util.Date());
        cicloActual = null;
    }

    private void deshabilitarFormulario() {
        textField1.setEnabled(false);
        spinner1.setEnabled(false);
        spinner2.setEnabled(false);
        guardarButton.setEnabled(false);
    }

    private void habilitarFormulario() {
        textField1.setEnabled(true);
        spinner1.setEnabled(true);
        spinner2.setEnabled(true);
        guardarButton.setEnabled(true);
    }

    private Date convertirFecha(Object value) {
        java.util.Date date = (java.util.Date) value;
        return new Date(date.getTime());
    }

    private void mostrarError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }



}
