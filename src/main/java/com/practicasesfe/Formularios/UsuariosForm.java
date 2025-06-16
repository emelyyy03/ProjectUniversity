package com.practicasesfe.Formularios;

import com.practicasesfe.Persistencia.UserDAO;
import com.practicasesfe.dominio.Ciclo;
import com.practicasesfe.dominio.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.util.List;
import javax.swing.JComboBox;

public class UsuariosForm extends JPanel {
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPass;
    private JComboBox<String> cboRol;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JTable table1;
    private JButton guardadButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JButton editarButton;
    private JButton crearButton;
    private JButton verButton;
    private JButton eliminarButton;
    private JPanel rootPanel;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private UserDAO userDAO;
    private User usuario;
    private boolean modoEditar = false;


    public UsuariosForm() {
        userDAO = new UserDAO();

        // Spinner para fechas
        spinner1.setModel(new SpinnerDateModel());
        spinner2.setModel(new SpinnerDateModel());
        spinner1.setEditor(new JSpinner.DateEditor(spinner1, "yyyy-MM-dd"));
        spinner2.setEditor(new JSpinner.DateEditor(spinner2, "yyyy-MM-dd"));

        // ComboBox de roles
        cboRol.addItem("Seleccione un rol");
        cboRol.addItem("admin");
        cboRol.addItem("docente");

        configurarEventos();
        cargarUsuarios();
    }

    private void configurarEventos() {
        crearButton.addActionListener(e -> {
            limpiarFormulario();
            habilitarFormulario();
            usuario = null;
            modoEditar = false;
        });

        guardadButton.addActionListener(e -> guardarUsuario());

        editarButton.addActionListener(e -> {
            if (usuario != null) {
                modoEditar = true;
                habilitarFormulario();
                llenarFormulario(usuario);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.");
            }
        });

        verButton.addActionListener(e -> {
            if (usuario != null) {
                llenarFormulario(usuario);
                deshabilitarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para ver.");
            }
        });

        eliminarButton.addActionListener(e -> eliminarUsuario());

        cancelarButton.addActionListener(e -> {
            limpiarFormulario();
            habilitarFormulario();
            modoEditar = false;
        });

        buscarButton.addActionListener(e -> buscarUsuarios());

        // Evento para seleccionar fila en la tabla
        table1.getSelectionModel().addListSelectionListener(e -> {
            int fila = table1.getSelectedRow();
            if (fila >= 0) {
                int idUsuario = (int) table1.getValueAt(fila, 0);
                try {
                    usuario = userDAO.getById(idUsuario);
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        });
    }


    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPass.getPassword()).trim();
        String rol = (String) cboRol.getSelectedItem();
        Date fechaCreacion = (Date) spinner1.getValue();
        Date fechaActualizacion = (Date) spinner2.getValue();

        // Validaciones básicas
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || rol.equals("Seleccione un rol")) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos correctamente.");
            return;
        }

        if (modoEditar && usuario != null) {
            usuario.setName(nombre);
            usuario.setEmail(email);
            usuario.setPasswordHash(password); // si lo hasheás, hacelo aquí
            usuario.setRol(rol);
            usuario.setFecha_creacion(fechaCreacion);
            usuario.setFecha_act(fechaActualizacion);

            try {
                if (userDAO.update(usuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        } else {
            User nuevoUsuario = new User();
            nuevoUsuario.setName(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPasswordHash(password); // aplicar hash si es necesario
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setFecha_creacion(fechaCreacion);
            nuevoUsuario.setFecha_act(fechaActualizacion);

            try {
                userDAO.create(nuevoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }

        limpiarFormulario();
        cargarUsuarios();
        modoEditar = false;
    }

    private void eliminarUsuario() {
        int filaSeleccionada = table1.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario en la tabla para eliminar");
            return;
        }

        // Suponiendo que el ID está en la primera columna
        int idUsuario = (int) table1.getValueAt(filaSeleccionada, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este usuario?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                User usuarioAEliminar = userDAO.getById(idUsuario);
                userDAO.delete(usuarioAEliminar);

                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
                limpiarFormulario();
                cargarUsuarios();

            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }


    private void buscarUsuarios() {
        String nombre = txtNombre.getText();
        try {
            List<User> lista = userDAO.search(nombre);
            cargarTabla(lista);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarUsuarios() {
        try {
            List<User> lista = userDAO.search("");
            cargarTabla(lista);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarTabla(List<User> lista) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Email", "Contraseña", "Rol", "Fecha Creación", "Fecha Actualización"}, 0
        );

        for (User u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(),                      // ID
                    u.getName(),                    // Nombre
                    u.getEmail(),                   // Email
                    u.getPasswordHash(),            // Contraseña (podés ocultarla si querés)
                    u.getRol(),                     // Rol
                    u.getFecha_creacion(),          // Fecha Creación
                    u.getFecha_act()                // Fecha Actualización
            });
        }

        table1.setModel(modelo);
    }

    private void llenarFormulario(User usuario) {
        this.usuario = usuario; // para que quede cargado en la variable global

        txtNombre.setText(usuario.getName());
        txtEmail.setText(usuario.getEmail());
        txtPass.setText(usuario.getPasswordHash()); // o "" si no querés mostrarla directamente
        cboRol.setSelectedItem(usuario.getRol());
        spinner1.setValue(usuario.getFecha_creacion());
        spinner2.setValue(usuario.getFecha_act());

        modoEditar = true; // activa modo edición
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtPass.setText("");
        //cboRol.
        spinner1.setValue(new java.util.Date());
        spinner2.setValue(new java.util.Date());
        usuario = null;
    }

    private void deshabilitarFormulario() {
        txtNombre.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPass.setEnabled(false);
        cboRol.setEnabled(false);
        spinner1.setEnabled(false);
        spinner2.setEnabled(false);
        guardadButton.setEnabled(false);
    }

    private void habilitarFormulario() {
        txtNombre.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPass.setEnabled(true);
        cboRol.setEnabled(true);
        spinner1.setEnabled(true);
        spinner2.setEnabled(true);
        guardadButton.setEnabled(true);
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
