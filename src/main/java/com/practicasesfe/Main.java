package com.practicasesfe;

import com.practicasesfe.Formularios.FormularioPrincipal;
import com.practicasesfe.Formularios.LoginForm;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        /*// correr en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pantalla Principal");

            // Crear instancia del formulario generado
            FormularioPrincipal mainForm = new FormularioPrincipal();

            // Establecer el contenido de la ventana principal
            frame.setContentPane(mainForm.getRootPanel());

            // Agregar la barra de menú desde código
            mainForm.agregarMenu(frame);

            // Configurar propiedades de la ventana
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null); // Centrar
            frame.setVisible(true);
            LoginForm loginForm = new LoginForm((mainForm));
            loginForm.setVisible(true);
        });*/

        SwingUtilities.invokeLater(() -> {
            // Crear un JFrame oculto solo para centrar el login (no se mostrará)
            JFrame dummyFrame = new JFrame();
            dummyFrame.setUndecorated(true); // Sin bordes
            dummyFrame.setSize(0, 0); // Invisible
            dummyFrame.setLocationRelativeTo(null);

            // Crear el formulario principal pero NO mostrarlo aún
            FormularioPrincipal mainForm = new FormularioPrincipal();

            // Mostrar login
            LoginForm loginForm = new LoginForm(mainForm);
            loginForm.setVisible(true); // este es modal, así que detiene la ejecución aquí

            // Solo si el login fue exitoso (usuario autenticado), mostrar la ventana principal
            if (mainForm.getUserAutenticate() != null) {
                JFrame frame = new JFrame("Pantalla Principal");
                frame.setContentPane(mainForm.getRootPanel());
                mainForm.agregarMenu(frame);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                System.exit(0); // login cancelado o inválido
            }
        });
    }
}