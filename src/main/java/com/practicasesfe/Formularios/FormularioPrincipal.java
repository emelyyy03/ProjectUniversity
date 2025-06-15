package com.practicasesfe.Formularios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormularioPrincipal {
    //private JPanel rootPanel;
    private JPanel contentPanel;
    private String opcionActual= "";

    private JPanel rootPanel = new JPanel(new BorderLayout());

    public FormularioPrincipal() {


        // Fondo rootPanel
        rootPanel.setOpaque(true);
        rootPanel.setBackground(new Color(210, 210, 210)); // fondo azul oscuro

        // Inicializar contentPanel
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(true);
        contentPanel.setBackground(new Color(210, 210, 210));

        // Agregar contentPanel al centro del rootPanel
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                String texto = "¡¡Bienvenidos a University App!!";
                Font font = new Font("Edwardian Script ITC", Font.BOLD, 85);
                g2.setFont(font);
                g2.setColor(Color.BLACK);

                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(texto);
                int textHeight = fm.getAscent();

                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2;

                g2.drawString(texto, x, y);
            }
        };
        panelBienvenida.setOpaque(false);
        contentPanel.add(panelBienvenida, "inicio");

        // Agregar formularios al contentPanel
        contentPanel.add(new PerfilForm(), "perfil");
        contentPanel.add(new UsuariosForm(), "usuarios");
        contentPanel.add(new Estudiantes(), "estudiantes");
        contentPanel.add(new DocentesForm(), "docentes");
        contentPanel.add(new MateriaForm(), "materia");
        contentPanel.add(new Horarios(), "horarios");
        contentPanel.add(new EstudianteHorario(), "estudianteHorario");
        contentPanel.add(new Ciclos().getRootPanel(), "ciclos");
        contentPanel.add(new AulasForm(), "aulas");
        contentPanel.add(new ProgramasAcademicosForm(), "programas");

        // Mostrar el panel base por defecto
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "inicio");

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    //Agregar iconos al titulo de los menus
    public void agregarMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(102, 0, 51)); // Fondo oscuro
        menuBar.setOpaque(true);

        // Menús con íconos
        JMenu menuPerfil = new JMenu("Mi Perfil");
        menuPerfil.setIcon(new ImageIcon(getClass().getResource("/icons/perfil.png")));

        JMenu menuUsuarios = new JMenu("Usuarios");
        menuUsuarios.setIcon(new ImageIcon(getClass().getResource("/icons/usuarios1.png")));

        JMenu menuEstudiantes = new JMenu("Estudiantes");
        menuEstudiantes.setIcon(new ImageIcon(getClass().getResource("/icons/estudiantes1.png")));

        JMenu menuDocentes = new JMenu("Docentes");
        menuDocentes.setIcon(new ImageIcon(getClass().getResource("/icons/docentes1.png")));

        JMenu menuMateria = new JMenu("Materia");
        menuMateria.setIcon(new ImageIcon(getClass().getResource("/icons/materias1.png")));

        JMenu menuHorarios = new JMenu("Horarios");
        menuHorarios.setIcon(new ImageIcon(getClass().getResource("/icons/horarios1.png")));

        JMenu menuEstudianteHorario = new JMenu("Estudiante Horario");
        menuEstudianteHorario.setIcon(new ImageIcon(getClass().getResource("/icons/horarioestudiantes1.png")));

        JMenu menuCiclos = new JMenu("Ciclos");
        menuCiclos.setIcon(new ImageIcon(getClass().getResource("/icons/ciclos1.png")));

        JMenu menuAulas = new JMenu("Aulas");
        menuAulas.setIcon(new ImageIcon(getClass().getResource("/icons/aulas1.png")));

        JMenu menuProgramas = new JMenu("Programas Académicos");
        menuProgramas.setIcon(new ImageIcon(getClass().getResource("/icons/programas1.png")));



        // Agregar menús
        menuBar.add(menuUsuarios);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuDocentes);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuMateria);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuEstudiantes);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuEstudianteHorario);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuHorarios);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuCiclos);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuAulas);
        menuBar.add(Box.createHorizontalStrut(5)); // separador
        menuBar.add(menuProgramas);
        menuBar.add(Box.createHorizontalGlue()); //empuja el boton al final
        menuBar.add(menuPerfil);

        // Estilos de menus
        Color fondoClaro = new Color(102, 0, 51);
        Color fondoHoverClaro = new Color(255, 255, 255, 30); // más elegante
        Color textoOscuro = Color.WHITE;
        Font fuente = new Font("Times New Roman", Font.PLAIN, 16);

        JMenu[] menus = {
                menuUsuarios, menuEstudiantes, menuDocentes, menuMateria,
                menuHorarios, menuEstudianteHorario, menuCiclos, menuAulas, menuProgramas
        };

        for (JMenu menu : menus) {
            menu.setFont(fuente);
            menu.setForeground(textoOscuro);
            menu.setOpaque(true);
            menu.setBackground(fondoClaro);
            menu.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));

            menu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        menu.setOpaque(true);
                        menu.setBackground(fondoHoverClaro);
                        menu.repaint(); // Evita glitches visuales
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        menu.setBackground(fondoClaro);
                        menu.repaint(); // Evita glitches visuales
                    });
                }
            });
        }

        // Estilo para "Mi Perfil"
        menuPerfil.setFont(new Font("Times New Roman", Font.BOLD, 16));
        menuPerfil.setForeground(Color.WHITE);
        menuPerfil.setOpaque(true);
        menuPerfil.setBackground(new Color(102, 0, 51));
        menuPerfil.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        menuPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuPerfil.setBackground(new Color(255, 255, 255, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuPerfil.setBackground(new Color(102, 0, 51));
            }
        });

        // Asociar eventos
        menuPerfil.addMouseListener(menuClick("perfil"));
        menuUsuarios.addMouseListener(menuClick("usuarios"));
        menuEstudiantes.addMouseListener(menuClick("estudiantes"));
        menuDocentes.addMouseListener(menuClick("docentes"));
        menuMateria.addMouseListener(menuClick("materia"));
        menuHorarios.addMouseListener(menuClick("horarios"));
        menuEstudianteHorario.addMouseListener(menuClick("estudianteHorario"));
        menuCiclos.addMouseListener(menuClick("ciclos"));
        menuAulas.addMouseListener(menuClick("aulas"));
        menuProgramas.addMouseListener(menuClick("programas"));

        frame.setJMenuBar(menuBar);
    }


    private MouseAdapter menuClick(String name) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                if (name.equals(opcionActual)) {
                    cl.show(contentPanel, "inicio"); // volver al panel base
                    opcionActual = "";
                } else {
                    cl.show(contentPanel, name); // mostrar formulario
                    opcionActual = name;
                }
            }
        };
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("Pantalla Principal");
        FormularioPrincipal mainForm = new FormularioPrincipal();

        frame.setContentPane(mainForm.getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        mainForm.agregarMenu(frame);
        frame.setVisible(true);
    }




}
