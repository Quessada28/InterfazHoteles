import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent.*;
import javax.swing.event.DocumentListener.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

//Investigar como canviarle el lookAndFeel

// Parte de Kevin
public class DashboardSwing extends JFrame {

    //Paleta de colores
    private final Color COLOR_CAFE = new Color(72, 54, 39);
    private final Color COLOR_CAFE_OSCURO = new Color(50, 34, 22);
    private final Color COLOR_BRONCE = new Color(174, 135, 76);
    private final Color COLOR_DORADO = new Color(189, 149, 90);
    private final Color COLOR_BEIGE = new Color(216, 209, 201);
    private final Color COLOR_BEIGE_CLARO = new Color(241, 236, 229);
    private final Color COLOR_FONDO = new Color(250, 248, 245);
    private final Color COLOR_BORDE = new Color(224, 216, 206);
    private final Color COLOR_TEXTO_SUAVE = new Color(128, 116, 104);
    private final Color COLOR_TEXTO_CLARO = new Color(226, 218, 208);
    private final Color COLOR_ESTADO = new Color(154, 176, 132);
    private final Color COLOR_ROJO = new Color(166, 78, 62);

    private final Font FUENTE_PORTADA = new Font("Georgia", Font.PLAIN, 42);
    private final Font FUENTE_TITULO_TARJETA = new Font("Georgia", Font.BOLD, 21);
    private final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font FUENTE_MENU = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FUENTE_MINI = new Font("Segoe UI", Font.PLAIN, 12);

    private static final String FUENTE_ICONOS = "Segoe UI Symbol";

    private static final String[] RUTAS_PORTADA = {
            "imagenes/portada.jpg",
            "ParteKevin/imagenes/portada.jpg",
            "src/imagenes/portada.jpg"
    };

    private static final String[][] SECCIONES = {
            {"INICIO", "⌂", "Inicio",
                    "Bienvenido", "Resumen general del hotel"},

            {"HABITACIONES", "▤", "Habitaciones",
                    "Habitaciones", "Registro y control de las habitaciones del hotel"},

            {"HUESPEDES", "☺", "Huéspedes",
                    "Huéspedes", "Administración de los huéspedes registrados"},

            {"RESERVACIONES", "★", "Reservaciones",
                    "Reservaciones", "Control de entradas, salidas y estado de las reservas"},

            {"REPORTES", "▥", "Reportes",
                    "Reportes", "Consultas y resúmenes de la operación del hotel"},

            {"ACERCA", "◆", "Acerca de",
                    "Acerca de", "Información del sistema y de sus desarrolladores"}
    };

    private final CardLayout gestorTarjetas = new CardLayout();
    private final JPanel contenedorPaneles = new JPanel(gestorTarjetas);
    private final JButton[] botonesMenu = new JButton[SECCIONES.length];

    private String seccionActiva = "INICIO";

    private JScrollPane desplazamientoInicio;
    private JLabel lblTituloPortada;
    private JLabel lblRutaPortada;
    private JLabel lblSubtitulo;
    private JLabel lblReloj;
    private JLabel lblEstado;

    public DashboardSwing() {

        configurarLookAndFeel();

        setTitle("Royelle Luxury Hotel - Sistema de Gestión Hotelera");
        setSize(1500, 930);
        setMinimumSize(new Dimension(1250, 760));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenidoPrincipal(), BorderLayout.CENTER);
        add(crearPieDePagina(), BorderLayout.SOUTH);

        mostrarSeccion("INICIO");
        iniciarReloj();

        SwingUtilities.invokeLater(
                () -> desplazamientoInicio.getVerticalScrollBar().setValue(0));
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el Look & Feel del sistema: " + e.getMessage());
        }
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(COLOR_CAFE_OSCURO);

        encabezado.add(crearBarraContacto(), BorderLayout.NORTH);
        encabezado.add(crearPortada(), BorderLayout.CENTER);

        return encabezado;
    }

    private JPanel crearBarraContacto() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_CAFE_OSCURO);
        barra.setBorder(new EmptyBorder(10, 28, 10, 28));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        izquierda.setOpaque(false);

        izquierda.add(crearDatoContacto("✆", "(506) 2222-0000"));
        izquierda.add(crearDatoContacto("✉", "reservaciones@royelle.com"));
        izquierda.add(crearDatoContacto("⌖", "Playa Hermosa, Guanacaste, Costa Rica"));

        JLabel lblSesion = new JLabel(textoConIcono("☺", "Recepción  ·  Administrador"));
        lblSesion.setFont(FUENTE_MINI);
        lblSesion.setForeground(COLOR_TEXTO_CLARO);

        barra.add(izquierda, BorderLayout.WEST);
        barra.add(lblSesion, BorderLayout.EAST);

        return barra;
    }

    private JLabel crearDatoContacto(String icono, String texto) {

        JLabel dato = new JLabel(textoConIcono(icono, texto));
        dato.setFont(FUENTE_MINI);
        dato.setForeground(COLOR_TEXTO_CLARO);

        return dato;
    }

    private JPanel crearPortada() {

        PanelPortada portada = new PanelPortada();

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblMarca = new JLabel("R O Y E L L E   L U X U R Y   H O T E L");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMarca.setForeground(COLOR_DORADO);
        lblMarca.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTituloPortada = new JLabel("Bienvenido");
        lblTituloPortada.setFont(FUENTE_PORTADA);
        lblTituloPortada.setForeground(Color.WHITE);
        lblTituloPortada.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(COLOR_DORADO);
        linea.setMaximumSize(new Dimension(70, 2));
        linea.setPreferredSize(new Dimension(70, 2));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRutaPortada = new JLabel("Inicio");
        lblRutaPortada.setFont(FUENTE_NORMAL);
        lblRutaPortada.setForeground(new Color(232, 226, 216));
        lblRutaPortada.setAlignmentX(Component.CENTER_ALIGNMENT);

        textos.add(lblMarca);
        textos.add(Box.createVerticalStrut(16));
        textos.add(lblTituloPortada);
        textos.add(Box.createVerticalStrut(18));
        textos.add(linea);
        textos.add(Box.createVerticalStrut(18));
        textos.add(lblRutaPortada);

        portada.add(textos);

        return portada;
    }

    private JPanel crearContenidoPrincipal() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(COLOR_FONDO);

        principal.add(crearBarraSeccion(), BorderLayout.NORTH);

        contenedorPaneles.setBackground(COLOR_FONDO);

        contenedorPaneles.add(crearPanelInicio(), "INICIO");

        contenedorPaneles.add(crearPanelPendiente(
                "Módulo de Habitaciones",
                "En esta sección se colocará el formulario de habitaciones (número, tipo, "
                        + "precio por noche, capacidad, estado y amenidades), la JTable con los "
                        + "registros, los botones Nuevo / Guardar / Editar / Eliminar / Limpiar "
                        + "y el buscador dinámico.",
                "Corresponde al punto 4 de la parte de Kevin."), "HABITACIONES");

        contenedorPaneles.add(crearPanelPendiente(
                "Módulo de Huéspedes",
                "En esta sección se colocará el formulario de registro de huéspedes junto con "
                        + "su tabla, sus validaciones y sus operaciones CRUD.",
                "Corresponde a la parte de Luis."), "HUESPEDES");

        contenedorPaneles.add(crearPanelPendiente(
                "Módulo de Reservaciones",
                "En esta sección se colocará el registro de reservaciones con número y tipo de "
                        + "habitación, fechas de entrada y salida, estado de la reserva y su tabla "
                        + "con búsqueda.",
                "Corresponde a la parte de Luis."), "RESERVACIONES");

        contenedorPaneles.add(crearPanelPendiente(
                "Reportes del hotel",
                "En esta sección se mostrarán los resúmenes de ocupación, reservaciones e "
                        + "ingresos generados por el hotel.",
                "Sección complementaria del sistema."), "REPORTES");

        contenedorPaneles.add(crearPanelAcercaDe(), "ACERCA");

        principal.add(contenedorPaneles, BorderLayout.CENTER);

        return principal;
    }

    private JPanel crearBarraSeccion() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, COLOR_BORDE),
                new EmptyBorder(16, 30, 16, 30)
        ));

        lblSubtitulo = new JLabel();
        lblSubtitulo.setFont(FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(COLOR_TEXTO_SUAVE);

        PanelRedondeado marcoReloj = new PanelRedondeado(
                new FlowLayout(FlowLayout.CENTER, 0, 0), COLOR_BEIGE_CLARO, null, 22);
        marcoReloj.setBorder(new EmptyBorder(10, 22, 10, 22));

        lblReloj = new JLabel();
        lblReloj.setFont(FUENTE_NORMAL);
        lblReloj.setForeground(COLOR_CAFE);

        marcoReloj.add(lblReloj);

        barra.add(lblSubtitulo, BorderLayout.WEST);
        barra.add(marcoReloj, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPieDePagina() {

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(COLOR_CAFE_OSCURO);

        pie.add(crearBarraNavegacion(), BorderLayout.NORTH);
        pie.add(crearBarraEstado(), BorderLayout.SOUTH);

        return pie;
    }

    private JPanel crearBarraNavegacion() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, COLOR_BORDE),
                new EmptyBorder(14, 26, 14, 26)
        ));

        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        opciones.setOpaque(false);

        for (int i = 0; i < SECCIONES.length; i++) {

            JButton boton = crearBotonMenu(
                    SECCIONES[i][1],
                    SECCIONES[i][2],
                    SECCIONES[i][0]);

            botonesMenu[i] = boton;

            opciones.add(boton);
        }

        barra.add(opciones, BorderLayout.CENTER);
        barra.add(crearBotonCerrarSesion(), BorderLayout.EAST);

        return barra;
    }

    private JButton crearBotonMenu(String icono, String texto, String clave) {

        BotonRedondeado boton = new BotonRedondeado(
                textoConIcono(icono, texto.toUpperCase()), 22, null);

        boton.setFont(FUENTE_MENU);
        boton.setForeground(COLOR_CAFE);
        boton.setBackground(Color.WHITE);
        boton.setBorder(new EmptyBorder(13, 20, 13, 20));

        boton.addActionListener(e -> mostrarSeccion(clave));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!clave.equals(seccionActiva)) {
                    boton.setBackground(COLOR_BEIGE_CLARO);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!clave.equals(seccionActiva)) {
                    boton.setBackground(Color.WHITE);
                }
            }
        });

        return boton;
    }

    private JButton crearBotonCerrarSesion() {

        BotonRedondeado boton = new BotonRedondeado(
                textoConIcono("⇥", "CERRAR SESIÓN"), 22, COLOR_BRONCE);

        boton.setFont(FUENTE_MENU);
        boton.setForeground(COLOR_CAFE);
        boton.setBackground(Color.WHITE);
        boton.setBorder(new EmptyBorder(13, 22, 13, 22));

        boton.addActionListener(e -> cerrarSesion());

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_ROJO);
                boton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(Color.WHITE);
                boton.setForeground(COLOR_CAFE);
            }
        });

        return boton;
    }

    private JPanel crearBarraEstado() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_CAFE_OSCURO);
        barra.setBorder(new EmptyBorder(13, 28, 13, 28));

        lblEstado = new JLabel("●  Sistema listo");
        lblEstado.setFont(FUENTE_PEQUENA);
        lblEstado.setForeground(COLOR_ESTADO);

        JLabel lblCreditos = new JLabel(
                "Royelle Luxury Hotel   ·   Proyecto Java Swing   ·   Kevin y Luis");
        lblCreditos.setFont(FUENTE_PEQUENA);
        lblCreditos.setForeground(new Color(176, 163, 148));

        barra.add(lblEstado, BorderLayout.WEST);
        barra.add(lblCreditos, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPanelInicio() {

        JPanel contenido = new JPanel(new BorderLayout(0, 20));
        contenido.setBackground(COLOR_FONDO);
        contenido.setBorder(new EmptyBorder(24, 28, 24, 28));

        contenido.add(crearTarjetaAviso(
                "Tarjetas indicadoras",
                "Aquí se colocarán las cuatro tarjetas del dashboard: habitaciones disponibles, "
                        + "habitaciones ocupadas, reservaciones activas e ingresos del mes.",
                "Corresponde al punto 3 de la parte de Kevin."), BorderLayout.NORTH);

        contenido.add(crearTarjetaServicios(), BorderLayout.CENTER);

        desplazamientoInicio = new JScrollPane(contenido);
        desplazamientoInicio.setBorder(null);
        desplazamientoInicio.getViewport().setBackground(COLOR_FONDO);
        desplazamientoInicio.getVerticalScrollBar().setUnitIncrement(16);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.add(desplazamientoInicio, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaServicios() {

        PanelRedondeado tarjeta = new PanelRedondeado(
                new BorderLayout(0, 18), Color.WHITE, COLOR_BORDE, 10);
        tarjeta.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel etiqueta = new JLabel("N U E S T R O S   S E R V I C I O S");
        etiqueta.setFont(FUENTE_MINI);
        etiqueta.setForeground(COLOR_BRONCE);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Bienestar y comodidad para cada huésped");
        titulo.setFont(FUENTE_TITULO_TARJETA);
        titulo.setForeground(COLOR_CAFE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "Comodidades incluidas en todas las habitaciones del hotel.");
        descripcion.setFont(FUENTE_NORMAL);
        descripcion.setForeground(COLOR_TEXTO_SUAVE);
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(etiqueta);
        encabezado.add(Box.createVerticalStrut(10));
        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(descripcion);

        JPanel lista = new JPanel(new GridLayout(2, 3, 26, 18));
        lista.setOpaque(false);

        lista.add(crearServicio("❄", "Aire acondicionado"));
        lista.add(crearServicio("▭", "TV pantalla plana"));
        lista.add(crearServicio("≈", "Wi-Fi de alta velocidad"));
        lista.add(crearServicio("⚿", "Caja fuerte electrónica"));
        lista.add(crearServicio("☕", "Servicio a la habitación"));
        lista.add(crearServicio("♨", "Aguas termales y spa"));

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(lista, BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearServicio(String icono, String texto) {

        JPanel servicio = new JPanel(new BorderLayout(14, 0));
        servicio.setOpaque(false);

        JLabel caja = new JLabel(icono, SwingConstants.CENTER);
        caja.setFont(new Font(FUENTE_ICONOS, Font.PLAIN, 17));
        caja.setForeground(COLOR_BRONCE);
        caja.setPreferredSize(new Dimension(42, 42));
        caja.setBorder(new LineBorder(COLOR_BORDE, 1, true));

        JLabel nombre = new JLabel(texto);
        nombre.setFont(FUENTE_NORMAL);
        nombre.setForeground(COLOR_CAFE);

        JPanel centrado = new JPanel(new GridBagLayout());
        centrado.setOpaque(false);
        centrado.add(caja);

        servicio.add(centrado, BorderLayout.WEST);
        servicio.add(nombre, BorderLayout.CENTER);

        return servicio;
    }

    private JPanel crearPanelAcercaDe() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        PanelRedondeado tarjeta = new PanelRedondeado(
                new BorderLayout(0, 20), Color.WHITE, COLOR_BORDE, 10);
        tarjeta.setBorder(new EmptyBorder(30, 34, 30, 34));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Sistema de Gestión Hotelera Royelle");
        titulo.setFont(FUENTE_TITULO_TARJETA);
        titulo.setForeground(COLOR_CAFE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel subrayado = new JPanel();
        subrayado.setBackground(COLOR_DORADO);
        subrayado.setMaximumSize(new Dimension(64, 2));
        subrayado.setPreferredSize(new Dimension(64, 2));
        subrayado.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(12));
        encabezado.add(subrayado);

        JPanel datos = new JPanel(new GridBagLayout());
        datos.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 26);
        gbc.anchor = GridBagConstraints.WEST;

        agregarDato(datos, gbc, 0, "Versión:", "1.0.0");
        agregarDato(datos, gbc, 1, "Curso:", "Programación con Java - Interfaces gráficas");
        agregarDato(datos, gbc, 2, "Desarrollado por:", "Kevin y Luis");
        agregarDato(datos, gbc, 3, "Tecnología:", "Java Swing");
        agregarDato(datos, gbc, 4, "Sistema operativo:", System.getProperty("os.name"));
        agregarDato(datos, gbc, 5, "Versión de Java:", System.getProperty("java.version"));

        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setOpaque(false);
        envoltura.add(datos, BorderLayout.WEST);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(envoltura, BorderLayout.CENTER);

        panel.add(tarjeta, BorderLayout.NORTH);

        return panel;
    }

    private void agregarDato(JPanel panel, GridBagConstraints gbc,
                             int fila, String etiqueta, String valor) {

        gbc.gridx = 0;
        gbc.gridy = fila;

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(COLOR_CAFE);
        panel.add(lblEtiqueta, gbc);

        gbc.gridx = 1;

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(FUENTE_NORMAL);
        lblValor.setForeground(COLOR_TEXTO_SUAVE);
        panel.add(lblValor, gbc);
    }

    private JPanel crearPanelPendiente(String titulo, String descripcion, String responsable) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        panel.add(crearTarjetaAviso(titulo, descripcion, responsable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaAviso(String titulo, String descripcion, String nota) {

        PanelRedondeado tarjeta = new PanelRedondeado(
                new GridBagLayout(), Color.WHITE, COLOR_BORDE, 10);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(28, 40, 28, 40));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TITULO_TARJETA);
        lblTitulo.setForeground(COLOR_CAFE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subrayado = new JPanel();
        subrayado.setBackground(COLOR_DORADO);
        subrayado.setMaximumSize(new Dimension(64, 2));
        subrayado.setPreferredSize(new Dimension(64, 2));
        subrayado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(
                "<html><div style='text-align:center; width:540px;'>" + descripcion + "</div></html>");
        lblDescripcion.setFont(FUENTE_NORMAL);
        lblDescripcion.setForeground(COLOR_TEXTO_SUAVE);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNota = new JLabel(nota);
        lblNota.setFont(FUENTE_PEQUENA);
        lblNota.setForeground(COLOR_BRONCE);
        lblNota.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(14));
        contenido.add(subrayado);
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(lblDescripcion);
        contenido.add(Box.createVerticalStrut(18));
        contenido.add(lblNota);

        tarjeta.add(contenido);

        return tarjeta;
    }

    private void mostrarSeccion(String clave) {

        seccionActiva = clave;

        gestorTarjetas.show(contenedorPaneles, clave);

        for (int i = 0; i < SECCIONES.length; i++) {

            boolean activo = SECCIONES[i][0].equals(clave);

            if (activo) {
                lblTituloPortada.setText(SECCIONES[i][3]);
                lblRutaPortada.setText(clave.equals("INICIO")
                        ? "Inicio"
                        : "<html>Inicio&nbsp;&nbsp;<span style='color:#BD955A'>|</span>&nbsp;&nbsp;"
                        + SECCIONES[i][2] + "</html>");
                lblSubtitulo.setText(SECCIONES[i][4]);
                lblEstado.setText("●  Sección activa: " + SECCIONES[i][2]);
            }

            botonesMenu[i].setText(textoConIcono(
                    SECCIONES[i][1], SECCIONES[i][2].toUpperCase(),
                    activo ? COLOR_CAFE_OSCURO : COLOR_BRONCE));

            botonesMenu[i].setBackground(activo ? COLOR_DORADO : Color.WHITE);
            botonesMenu[i].setForeground(activo ? Color.WHITE : COLOR_CAFE);
            botonesMenu[i].setFont(activo ? FUENTE_MENU.deriveFont(Font.BOLD) : FUENTE_MENU);
        }
    }

    private String textoConIcono(String icono, String texto) {
        return textoConIcono(icono, texto, COLOR_DORADO);
    }

    private String textoConIcono(String icono, String texto, Color colorIcono) {
        return "<html><span style='font-family:" + FUENTE_ICONOS
                + "; color:" + aHexadecimal(colorIcono) + "'>" + icono
                + "</span>&nbsp;&nbsp;&nbsp;" + texto + "</html>";
    }

    private String aHexadecimal(Color color) {
        return String.format("#%02X%02X%02X",
                color.getRed(), color.getGreen(), color.getBlue());
    }

    private void iniciarReloj() {

        Timer temporizador = new Timer(1000, e -> actualizarReloj());
        temporizador.start();

        actualizarReloj();
    }

    private void actualizarReloj() {

        Locale localeCostaRica = Locale.forLanguageTag("es-CR");

        DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                "EEEE, dd 'de' MMMM 'de' yyyy    hh:mm:ss a", localeCostaRica);

        lblReloj.setText(textoConIcono("⌚", LocalDateTime.now().format(formato), COLOR_BRONCE));
    }

    private void cerrarSesion() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar la sesión y salir del sistema?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sesión finalizada. ¡Hasta pronto!",
                    "Cerrar sesión",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        }
    }

    private void confirmarSalida() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir del sistema de gestión hotelera?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private class PanelPortada extends JPanel {

        private final Image imagen;

        private PanelPortada() {
            super(new GridBagLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(0, 258));
            imagen = cargarImagen();
        }

        private Image cargarImagen() {

            for (String ruta : RUTAS_PORTADA) {
                ImageIcon icono = new ImageIcon(ruta);
                if (icono.getIconWidth() > 0) {
                    return icono.getImage();
                }
            }

            java.net.URL recurso = DashboardSwing.class.getResource("/imagenes/portada.jpg");

            if (recurso != null) {
                ImageIcon icono = new ImageIcon(recurso);
                if (icono.getIconWidth() > 0) {
                    return icono.getImage();
                }
            }

            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int ancho = getWidth();
            int alto = getHeight();

            if (imagen != null) {

                int anchoImagen = imagen.getWidth(this);
                int altoImagen = imagen.getHeight(this);

                double escala = Math.max(
                        (double) ancho / anchoImagen,
                        (double) alto / altoImagen);

                int anchoFinal = (int) (anchoImagen * escala);
                int altoFinal = (int) (altoImagen * escala);

                g2.drawImage(imagen,
                        (ancho - anchoFinal) / 2,
                        (alto - altoFinal) / 2,
                        anchoFinal, altoFinal, this);

                g2.setPaint(new GradientPaint(
                        0, 0, new Color(50, 34, 22, 130),
                        0, alto, new Color(50, 34, 22, 205)));

            } else {

                g2.setPaint(new GradientPaint(
                        0, 0, COLOR_CAFE, ancho, alto, COLOR_BRONCE));
            }

            g2.fillRect(0, 0, ancho, alto);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class PanelRedondeado extends JPanel {

        private final Color relleno;
        private final Color degradado;
        private final Color borde;
        private final int radio;

        private PanelRedondeado(LayoutManager distribucion, Color relleno, Color borde, int radio) {
            this(distribucion, relleno, null, borde, radio);
        }

        private PanelRedondeado(LayoutManager distribucion, Color relleno, Color degradado,
                                Color borde, int radio) {
            super(distribucion);
            this.relleno = relleno;
            this.degradado = degradado;
            this.borde = borde;
            this.radio = radio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (degradado != null) {
                g2.setPaint(new GradientPaint(0, 0, relleno, getWidth(), getHeight(), degradado));
            } else {
                g2.setPaint(relleno);
            }

            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);

            if (borde != null) {
                g2.setColor(borde);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class BotonRedondeado extends JButton {

        private final int radio;
        private final Color borde;

        private BotonRedondeado(String texto, int radio, Color borde) {
            super(texto);
            this.radio = radio;
            this.borde = borde;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);

            if (borde != null) {
                g2.setColor(borde);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }
    // Parte de Luis

    public class Modelos {
        public static class Huesped {

            public String identificacion;
            public String nombre;
            public String apellidos;
            public String telefono;
            public String correo;
            public String nacionalidad;
            public String observaciones;

            public String getNombreCompleto() {
                return nombre + " " + apellidos;
            }

            @Override
            public String toString() {
                return identificacion + "-" + getNombreCompleto();
            }
        }


        public static class Habitacion {

            public String numero;
            public String tipo;
            public double precioPorNoche;
            public int capacidad;
            public String estado;

            @Override
            public String toString() {
                return numero + "-" + tipo + "(" + precioPorNoche + " / noche)";
            }
        }

        public static class Reservacion {
            public int id;
            public Modelos.Huesped huesped;
            public Habitacion habitacion;
            public Date fechaEntrada;
            public Date fechaSalida;
            public int adultos;
            public int ninos;
            public int cantidadHabitaciones;
            public String estado;
            public boolean desayuno;
            public boolean parqueo;
            public boolean spa;
            public double total;

            public int getTotalHuespedes() {
                return adultos + ninos;
            }
        }
    }


//Paneles para la interfaz.

    public class PanelHuespedes extends JFrame{

        public PanelHuespedes(){

            setTitle("Modulo de Huesped");
            setSize(1100, 620);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private JPanel crearPanelRedondeado() {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(new LineBorder(new Color(218,223,230), 1, true));
            return panel;
        }
    }
}
