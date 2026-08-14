import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.event.DocumentEvent.*;
import javax.swing.event.DocumentListener.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

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

            {"HUESPEDES", "☺", "Huespedes",
                    "Huespedes", "Administracion de los huespedes registrados"},

            {"RESERVACIONES", "★", "Reservaciones",
                    "Reservaciones", "Control de entradas, salidas y estado de las reservas"},

            {"REPORTES", "▥", "Reportes",
                    "Reportes", "Consultas y resumenes de la operacion del hotel"},

            {"ACERCA", "◆", "Acerca de",
                    "Acerca de", "Informacion del sistema y de sus desarrolladores"}
    };

    private final CardLayout gestorTarjetas = new CardLayout();
    private final JPanel contenedorPaneles = new JPanel(gestorTarjetas);
    private final JButton[] botonesMenu = new JButton[SECCIONES.length];

    private String seccionActiva = "INICIO";

    private JScrollPane desplazamientoInicio;
    private PanelPortada portada;

    private PanelHabitaciones panelHabitaciones;
    private PanelReportes panelReportes;

    private JLabel lblIndicadorDisponibles;
    private JLabel lblIndicadorOcupadas;
    private JLabel lblIndicadorReservaciones;
    private JLabel lblIndicadorIngresos;

    private JLabel lblTituloPortada;
    private JLabel lblRutaPortada;
    private JLabel lblSubtitulo;
    private JLabel lblReloj;
    private JLabel lblEstado;

    public DashboardSwing() {

        configurarLookAndFeel();

        setTitle("Tabacon Hotel - Sistema de Gestion Hotelera");
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
        izquierda.add(crearDatoContacto("✉", "TabaconHotel@gmail.com"));
        izquierda.add(crearDatoContacto("⌖", "La Fortuna, San Jose, Costa Rica"));

        JLabel lblSesion = new JLabel(textoConIcono("☺", "Recepcion  ·  Administrador"));
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

        portada = new PanelPortada();

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblMarca = new JLabel("T A B A C O N   L U X U R Y   H O T E L");
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

        panelHabitaciones = new PanelHabitaciones(this);

        contenedorPaneles.add(panelHabitaciones, "HABITACIONES");

        panelReportes = new PanelReportes();

        contenedorPaneles.add(crearPanelPendiente(
                "Modulo de Huespedes",
                "En esta seccion se colocara el formulario de registro de huespedes junto con "
                        + "su tabla, sus validaciones y sus operaciones CRUD.",
                ""), "HUESPEDES");

        contenedorPaneles.add(crearPanelPendiente(
                "Modulo de Reservaciones",
                "En esta seccion se colocara el registro de reservaciones con numero y tipo de "
                        + "habitacion, fechas de entrada y salida, estado de la reserva y su tabla "
                        + "con busqueda.",
                ""), "RESERVACIONES");

        contenedorPaneles.add(panelReportes, "REPORTES");

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
                textoConIcono("⇥", "CERRAR SESION"), 22, COLOR_BRONCE);

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
                "Tabacon Hotel   ·   Proyecto Java Swing   ·   Kevin y Luis");
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

        contenido.add(crearTarjetasIndicadoras(), BorderLayout.NORTH);

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

    private JPanel crearTarjetasIndicadoras() {

        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 18, 0));
        tarjetas.setOpaque(false);
        tarjetas.setPreferredSize(new Dimension(0, 122));

        lblIndicadorDisponibles = new JLabel("0");
        lblIndicadorOcupadas = new JLabel("0");
        lblIndicadorReservaciones = new JLabel("0");
        lblIndicadorIngresos = new JLabel("$0");

        tarjetas.add(crearTarjetaIndicador("⌂", "HABITACIONES DISPONIBLES",
                lblIndicadorDisponibles, "Listas para reservar"));
        tarjetas.add(crearTarjetaIndicador("▤", "HABITACIONES OCUPADAS",
                lblIndicadorOcupadas, "Con huespedes dentro"));
        tarjetas.add(crearTarjetaIndicador("★", "RESERVACIONES ACTIVAS",
                lblIndicadorReservaciones, "En curso este mes"));
        tarjetas.add(crearTarjetaIndicador("♨", "INGRESOS DEL MES",
                lblIndicadorIngresos, "Acumulado del mes"));

        actualizarTarjetas();

        return tarjetas;
    }

    private JPanel crearTarjetaIndicador(String icono, String titulo, JLabel numero, String nota) {

        PanelRedondeado tarjeta = new PanelRedondeado(
                new BorderLayout(0, 6), Color.WHITE, COLOR_BORDE, 10);
        tarjeta.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTitulo = new JLabel(textoConIcono(icono, titulo, COLOR_BRONCE));
        lblTitulo.setFont(FUENTE_MINI);
        lblTitulo.setForeground(COLOR_TEXTO_SUAVE);

        numero.setFont(new Font("Georgia", Font.BOLD, 30));
        numero.setForeground(COLOR_CAFE);

        JLabel lblNota = new JLabel(nota);
        lblNota.setFont(FUENTE_MINI);
        lblNota.setForeground(COLOR_TEXTO_SUAVE);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(numero, BorderLayout.CENTER);
        tarjeta.add(lblNota, BorderLayout.SOUTH);

        return tarjeta;
    }

    public void actualizarTarjetas() {

        if (lblIndicadorDisponibles == null) {
            return;
        }

        lblIndicadorDisponibles.setText(
                String.valueOf(DatosHotel.contarHabitaciones(Habitacion.DISPONIBLE)));
        lblIndicadorOcupadas.setText(
                String.valueOf(DatosHotel.contarHabitaciones(Habitacion.OCUPADA)));
        lblIndicadorReservaciones.setText(
                String.valueOf(DatosHotel.contarReservaciones(Reservacion.ACTIVA)));
        lblIndicadorIngresos.setText(
                String.format(Locale.US, "$%,.0f", DatosHotel.calcularIngresos()));
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

        JLabel titulo = new JLabel("Bienestar y comodidad para cada huesped");
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
        lista.add(crearServicio("⚿", "Caja fuerte electronica"));
        lista.add(crearServicio("☕", "Servicio a la habitacion"));
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

        JLabel titulo = new JLabel("Sistema de Gestion Hotelera Tabacon");
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

        agregarDato(datos, gbc, 0, "Version:", "1.0.0");
        agregarDato(datos, gbc, 1, "Curso:", "Programacion III");
        agregarDato(datos, gbc, 2, "Desarrollado por:", "Kevin y Luis ");
        agregarDato(datos, gbc, 3, "Tecnologia:", "Java Swing");
        agregarDato(datos, gbc, 4, "Sistema operativo:", System.getProperty("os.name"));
        agregarDato(datos, gbc, 5, "Version de Java:", System.getProperty("java.version"));

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

        boolean esInicio = clave.equals("INICIO");

        portada.setPreferredSize(new Dimension(0, esInicio ? 258 : 112));
        portada.revalidate();

        if (esInicio) {
            actualizarTarjetas();
        }

        if (clave.equals("REPORTES")) {
            panelReportes.actualizarReporte();
        }

        for (int i = 0; i < SECCIONES.length; i++) {

            boolean activo = SECCIONES[i][0].equals(clave);

            if (activo) {
                lblTituloPortada.setText(SECCIONES[i][3]);
                lblRutaPortada.setText(clave.equals("INICIO")
                        ? "Inicio"
                        : "<html>Inicio&nbsp;&nbsp;<span style='color:#BD955A'>|</span>&nbsp;&nbsp;"
                        + SECCIONES[i][2] + "</html>");
                lblSubtitulo.setText(SECCIONES[i][4]);
                lblEstado.setText("●  Seccion activa: " + SECCIONES[i][2]);
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
                "¿Desea cerrar la sesion y salir del sistema?",
                "Cerrar sesion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sesion finalizada. ¡Hasta pronto!",
                    "Cerrar sesion",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        }
    }

    private void confirmarSalida() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir del sistema de gestion hotelera?",
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

    private static class EstiloHotel {

        public static final Color CAFE = new Color(72, 54, 39);
        public static final Color CAFE_OSCURO = new Color(50, 34, 22);
        public static final Color BRONCE = new Color(174, 135, 76);
        public static final Color DORADO = new Color(189, 149, 90);
        public static final Color BEIGE = new Color(216, 209, 201);
        public static final Color BEIGE_CLARO = new Color(241, 236, 229);
        public static final Color FONDO = new Color(250, 248, 245);
        public static final Color BORDE = new Color(224, 216, 206);
        public static final Color TEXTO_SUAVE = new Color(128, 116, 104);
        public static final Color VERDE = new Color(96, 122, 74);
        public static final Color ROJO = new Color(166, 78, 62);

        public static final Font TITULO = new Font("Georgia", Font.BOLD, 21);
        public static final Font SUBTITULO = new Font("Segoe UI", Font.PLAIN, 14);
        public static final Font NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
        public static final Font PEQUENA = new Font("Segoe UI", Font.PLAIN, 13);
        public static final Font ETIQUETA = new Font("Segoe UI", Font.BOLD, 13);
        public static final Font MINI = new Font("Segoe UI", Font.PLAIN, 12);
        public static final Font TOTAL = new Font("Georgia", Font.BOLD, 26);

        public static final String FUENTE_ICONOS = "Segoe UI Symbol";

        private EstiloHotel() {
        }

        public static PanelRedondeado crearPanelRedondeado(LayoutManager distribucion,
                                                           Color relleno, Color borde, int radio) {
            return new PanelRedondeado(distribucion, relleno, borde, radio);
        }

        public static PanelRedondeado crearTarjeta(LayoutManager distribucion) {

            PanelRedondeado tarjeta = new PanelRedondeado(distribucion, Color.WHITE, BORDE, 10);
            tarjeta.setBorder(new EmptyBorder(20, 24, 20, 24));

            return tarjeta;
        }

        public static JLabel crearEtiquetaSeccion(String texto) {

            JLabel etiqueta = new JLabel(texto);
            etiqueta.setFont(MINI);
            etiqueta.setForeground(BRONCE);
            etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

            return etiqueta;
        }

        public static JLabel crearTitulo(String texto) {

            JLabel titulo = new JLabel(texto);
            titulo.setFont(TITULO);
            titulo.setForeground(CAFE);
            titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

            return titulo;
        }

        public static JLabel crearSubtitulo(String texto) {

            JLabel subtitulo = new JLabel(texto);
            subtitulo.setFont(SUBTITULO);
            subtitulo.setForeground(TEXTO_SUAVE);
            subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

            return subtitulo;
        }

        public static JPanel crearLineaDorada() {

            JPanel linea = new JPanel();
            linea.setBackground(DORADO);
            linea.setPreferredSize(new Dimension(58, 2));
            linea.setMaximumSize(new Dimension(58, 2));
            linea.setAlignmentX(Component.LEFT_ALIGNMENT);

            return linea;
        }

        public static JPanel crearEncabezado(String etiqueta, String titulo, String descripcion) {

            JPanel encabezado = new JPanel();
            encabezado.setOpaque(false);
            encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

            encabezado.add(crearEtiquetaSeccion(etiqueta));
            encabezado.add(Box.createVerticalStrut(8));
            encabezado.add(crearTitulo(titulo));
            encabezado.add(Box.createVerticalStrut(10));
            encabezado.add(crearLineaDorada());
            encabezado.add(Box.createVerticalStrut(10));
            encabezado.add(crearSubtitulo(descripcion));

            return encabezado;
        }

        public static JPanel crearEncabezado(String etiqueta, String titulo) {
            return crearEncabezadoCompacto(etiqueta, titulo);
        }

        public static JPanel crearEncabezadoCompacto(String etiqueta, String titulo) {

            JPanel encabezado = new JPanel();
            encabezado.setOpaque(false);
            encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

            encabezado.add(crearEtiquetaSeccion(etiqueta));
            encabezado.add(Box.createVerticalStrut(6));
            encabezado.add(crearTitulo(titulo));
            encabezado.add(Box.createVerticalStrut(8));
            encabezado.add(crearLineaDorada());

            return encabezado;
        }

        public static JTextField crearCampoTexto() {

            JTextField campo = new JTextField();
            campo.setFont(NORMAL);
            campo.setForeground(CAFE);
            campo.setPreferredSize(new Dimension(175, 32));
            campo.setBorder(new CompoundBorder(
                    new LineBorder(BORDE, 1, true),
                    new EmptyBorder(4, 9, 4, 9)));

            return campo;
        }

        public static JScrollPane crearAreaTexto(JTextArea area, int filas) {

            area.setRows(filas);
            area.setFont(NORMAL);
            area.setForeground(CAFE);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setBorder(new EmptyBorder(6, 8, 6, 8));

            JScrollPane scroll = new JScrollPane(area);
            scroll.setBorder(new LineBorder(BORDE, 1, true));
            scroll.setPreferredSize(new Dimension(175, 66));

            return scroll;
        }

        public static void configurarCombo(JComboBox<?> combo) {

            combo.setFont(NORMAL);
            combo.setForeground(CAFE);
            combo.setBackground(Color.WHITE);
            combo.setPreferredSize(new Dimension(175, 32));
        }

        public static void configurarSpinner(JSpinner spinner) {

            spinner.setFont(NORMAL);
            spinner.setPreferredSize(new Dimension(175, 32));

            JComponent editor = spinner.getEditor();

            if (editor instanceof JSpinner.DefaultEditor) {
                ((JSpinner.DefaultEditor) editor).getTextField().setFont(NORMAL);
                ((JSpinner.DefaultEditor) editor).getTextField().setForeground(CAFE);
            }
        }

        public static JCheckBox crearCasilla(String texto) {

            JCheckBox casilla = new JCheckBox(texto);
            casilla.setFont(NORMAL);
            casilla.setForeground(CAFE);
            casilla.setOpaque(false);
            casilla.setFocusPainted(false);
            casilla.setCursor(new Cursor(Cursor.HAND_CURSOR));

            return casilla;
        }

        public static JRadioButton crearOpcion(String texto, ButtonGroup grupo, boolean seleccionado) {

            JRadioButton opcion = new JRadioButton(texto, seleccionado);
            opcion.setFont(NORMAL);
            opcion.setForeground(CAFE);
            opcion.setOpaque(false);
            opcion.setFocusPainted(false);
            opcion.setCursor(new Cursor(Cursor.HAND_CURSOR));

            grupo.add(opcion);

            return opcion;
        }

        public static void agregarFilaFormulario(JPanel formulario, GridBagConstraints reglas,
                                                 int fila, String etiqueta, Component componente) {

            agregarFilaFormulario(formulario, reglas, fila, 0, etiqueta, componente);
        }

        public static void agregarFilaFormulario(JPanel formulario, GridBagConstraints reglas,
                                                 int fila, int columna, String etiqueta,
                                                 Component componente) {

            reglas.gridx = columna * 2;
            reglas.gridy = fila;
            reglas.gridwidth = 1;
            reglas.weightx = 0;
            reglas.fill = GridBagConstraints.HORIZONTAL;
            reglas.anchor = GridBagConstraints.WEST;
            reglas.insets = new Insets(5, columna == 0 ? 0 : 20, 5, 10);

            JLabel lbl = new JLabel(etiqueta);
            lbl.setFont(ETIQUETA);
            lbl.setForeground(CAFE);

            formulario.add(lbl, reglas);

            reglas.gridx = columna * 2 + 1;
            reglas.weightx = 1;
            reglas.insets = new Insets(5, 0, 5, 0);

            formulario.add(componente, reglas);
        }

        public static void agregarFilaAncha(JPanel formulario, GridBagConstraints reglas,
                                            int fila, String etiqueta, Component componente) {

            reglas.gridx = 0;
            reglas.gridy = fila;
            reglas.gridwidth = 1;
            reglas.weightx = 0;
            reglas.fill = GridBagConstraints.HORIZONTAL;
            reglas.anchor = GridBagConstraints.WEST;
            reglas.insets = new Insets(5, 0, 5, 10);

            JLabel lbl = new JLabel(etiqueta);
            lbl.setFont(ETIQUETA);
            lbl.setForeground(CAFE);

            formulario.add(lbl, reglas);

            reglas.gridx = 1;
            reglas.gridwidth = 3;
            reglas.weightx = 1;
            reglas.insets = new Insets(5, 0, 5, 0);

            formulario.add(componente, reglas);

            reglas.gridwidth = 1;
        }

        public static BotonRedondeado crearBoton(String texto, Color fondo, Color letra) {

            BotonRedondeado boton = new BotonRedondeado(texto, 20, null);
            boton.setFont(ETIQUETA);
            boton.setBackground(fondo);
            boton.setForeground(letra);
            boton.setBorder(new EmptyBorder(10, 18, 10, 18));

            Color original = fondo;
            Color encima = aclarar(fondo);

            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    boton.setBackground(encima);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    boton.setBackground(original);
                }
            });

            return boton;
        }

        public static BotonRedondeado crearBotonBorde(String texto, Color color) {

            BotonRedondeado boton = new BotonRedondeado(texto, 20, color);
            boton.setFont(ETIQUETA);
            boton.setBackground(Color.WHITE);
            boton.setForeground(color);
            boton.setBorder(new EmptyBorder(10, 18, 10, 18));

            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    boton.setBackground(BEIGE_CLARO);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    boton.setBackground(Color.WHITE);
                }
            });

            return boton;
        }

        private static Color aclarar(Color color) {
            return new Color(
                    Math.min(color.getRed() + 25, 255),
                    Math.min(color.getGreen() + 25, 255),
                    Math.min(color.getBlue() + 25, 255));
        }

        public static void configurarTabla(JTable tabla) {

            tabla.setFont(PEQUENA);
            tabla.setForeground(CAFE);
            tabla.setRowHeight(30);
            tabla.setGridColor(new Color(236, 231, 223));
            tabla.setShowVerticalLines(false);
            tabla.setSelectionBackground(BEIGE);
            tabla.setSelectionForeground(CAFE_OSCURO);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabla.setAutoCreateRowSorter(false);

            JTableHeader encabezado = tabla.getTableHeader();
            encabezado.setFont(ETIQUETA);
            encabezado.setBackground(BEIGE_CLARO);
            encabezado.setForeground(CAFE);
            encabezado.setPreferredSize(new Dimension(0, 34));
            encabezado.setReorderingAllowed(false);
        }

        public static JScrollPane crearScrollTabla(JTable tabla) {

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(new LineBorder(BORDE, 1, true));
            scroll.getViewport().setBackground(Color.WHITE);

            return scroll;
        }

        public static String textoConIcono(String icono, String texto) {
            return "<html><span style='font-family:" + FUENTE_ICONOS
                    + "; color:" + aHexadecimal(BRONCE) + "'>" + icono
                    + "</span>&nbsp;&nbsp;" + texto + "</html>";
        }

        public static String aHexadecimal(Color color) {
            return String.format("#%02X%02X%02X",
                    color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    private static class Habitacion {

        public static final String DISPONIBLE = "Disponible";
        public static final String OCUPADA = "Ocupada";
        public static final String MANTENIMIENTO = "Mantenimiento";

        private int numero;
        private String tipo;
        private double precioPorNoche;
        private int capacidad;
        private String estado;
        private boolean aireAcondicionado;
        private boolean wifi;
        private boolean television;
        private boolean cajaFuerte;
        private String descripcion;

        public Habitacion(int numero, String tipo, double precioPorNoche, int capacidad,
                          String estado, boolean aireAcondicionado, boolean wifi,
                          boolean television, boolean cajaFuerte, String descripcion) {
            this.numero = numero;
            this.tipo = tipo;
            this.precioPorNoche = precioPorNoche;
            this.capacidad = capacidad;
            this.estado = estado;
            this.aireAcondicionado = aireAcondicionado;
            this.wifi = wifi;
            this.television = television;
            this.cajaFuerte = cajaFuerte;
            this.descripcion = descripcion;
        }

        public int getNumero() {
            return numero;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public double getPrecioPorNoche() {
            return precioPorNoche;
        }

        public void setPrecioPorNoche(double precioPorNoche) {
            this.precioPorNoche = precioPorNoche;
        }

        public int getCapacidad() {
            return capacidad;
        }

        public void setCapacidad(int capacidad) {
            this.capacidad = capacidad;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public boolean tieneAireAcondicionado() {
            return aireAcondicionado;
        }

        public void setAireAcondicionado(boolean aireAcondicionado) {
            this.aireAcondicionado = aireAcondicionado;
        }

        public boolean tieneWifi() {
            return wifi;
        }

        public void setWifi(boolean wifi) {
            this.wifi = wifi;
        }

        public boolean tieneTelevision() {
            return television;
        }

        public void setTelevision(boolean television) {
            this.television = television;
        }

        public boolean tieneCajaFuerte() {
            return cajaFuerte;
        }

        public void setCajaFuerte(boolean cajaFuerte) {
            this.cajaFuerte = cajaFuerte;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean estaDisponible() {
            return DISPONIBLE.equals(estado);
        }

        public String getAmenidades() {

            String amenidades = "";

            if (aireAcondicionado) {
                amenidades = amenidades + "A/C  ";
            }

            if (wifi) {
                amenidades = amenidades + "Wi-Fi  ";
            }

            if (television) {
                amenidades = amenidades + "TV  ";
            }

            if (cajaFuerte) {
                amenidades = amenidades + "Caja fuerte";
            }

            return amenidades.trim().isEmpty() ? "Ninguna" : amenidades.trim();
        }

        @Override
        public String toString() {
            return numero + "  -  " + tipo + "  ($" + String.format("%.2f", precioPorNoche)
                    + " x noche, hasta " + capacidad + " personas)";
        }
    }

    private static class PanelHabitaciones extends JPanel {

        private static final String[] COLUMNAS = {
                "Numero", "Tipo", "Precio", "Capacidad", "Estado", "Amenidades"
        };

        private static final String[] TIPOS = {"Standard", "Deluxe", "Suite"};

        private final DashboardSwing ventana;

        private final JTextField txtNumero = EstiloHotel.crearCampoTexto();
        private final JTextField txtPrecio = EstiloHotel.crearCampoTexto();
        private final JComboBox<String> cmbTipo = new JComboBox<>(TIPOS);
        private final JSpinner spCapacidad = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));

        private final ButtonGroup grupoEstado = new ButtonGroup();
        private final JRadioButton rbDisponible =
                EstiloHotel.crearOpcion("Disponible", grupoEstado, true);
        private final JRadioButton rbOcupada =
                EstiloHotel.crearOpcion("Ocupada", grupoEstado, false);
        private final JRadioButton rbMantenimiento =
                EstiloHotel.crearOpcion("Mantenimiento", grupoEstado, false);

        private final JCheckBox chkAire = EstiloHotel.crearCasilla("A/C");
        private final JCheckBox chkWifi = EstiloHotel.crearCasilla("Wi-Fi");
        private final JCheckBox chkTelevision = EstiloHotel.crearCasilla("TV");
        private final JCheckBox chkCajaFuerte = EstiloHotel.crearCasilla("Caja fuerte");

        private final JTextArea txtDescripcion = new JTextArea();

        private final JTextField txtBuscar = EstiloHotel.crearCampoTexto();
        private final DefaultTableModel modeloTabla = crearModeloTabla();
        private final JTable tabla = new JTable(modeloTabla);
        private final TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modeloTabla);

        private Habitacion habitacionSeleccionada;

        public PanelHabitaciones(DashboardSwing ventana) {

            this.ventana = ventana;

            setLayout(new BorderLayout(20, 0));
            setBackground(EstiloHotel.FONDO);
            setBorder(new EmptyBorder(20, 24, 20, 24));

            add(crearTarjetaFormulario(), BorderLayout.WEST);
            add(crearTarjetaTabla(), BorderLayout.CENTER);

            refrescarTabla();
        }

        private DefaultTableModel crearModeloTabla() {

            return new DefaultTableModel(COLUMNAS, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
        }

        private JPanel crearTarjetaFormulario() {

            PanelRedondeado tarjeta = EstiloHotel.crearTarjeta(new BorderLayout(0, 16));
            tarjeta.setPreferredSize(new Dimension(660, 0));

            tarjeta.add(EstiloHotel.crearEncabezado(
                    "R E G I S T R O",
                    "Datos de la habitacion"), BorderLayout.NORTH);

            JPanel formulario = new JPanel(new GridBagLayout());
            formulario.setOpaque(false);

            GridBagConstraints reglas = new GridBagConstraints();

            EstiloHotel.configurarCombo(cmbTipo);
            EstiloHotel.configurarSpinner(spCapacidad);

            EstiloHotel.agregarFilaFormulario(formulario, reglas, 0, 0, "Numero:", txtNumero);
            EstiloHotel.agregarFilaFormulario(formulario, reglas, 0, 1, "Precio por noche:", txtPrecio);
            EstiloHotel.agregarFilaFormulario(formulario, reglas, 1, 0, "Tipo:", cmbTipo);
            EstiloHotel.agregarFilaFormulario(formulario, reglas, 1, 1, "Capacidad:", spCapacidad);
            EstiloHotel.agregarFilaAncha(formulario, reglas, 2, "Estado:", crearPanelEstado());
            EstiloHotel.agregarFilaAncha(formulario, reglas, 3, "Amenidades:", crearPanelAmenidades());
            EstiloHotel.agregarFilaAncha(formulario, reglas, 4, "Descripcion:",
                    EstiloHotel.crearAreaTexto(txtDescripcion, 3));

            JScrollPane desplazamiento = new JScrollPane(formulario);
            desplazamiento.setBorder(null);
            desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            desplazamiento.setOpaque(false);
            desplazamiento.getViewport().setOpaque(false);
            desplazamiento.getVerticalScrollBar().setUnitIncrement(16);

            tarjeta.add(desplazamiento, BorderLayout.CENTER);
            tarjeta.add(crearBotones(), BorderLayout.SOUTH);

            return tarjeta;
        }

        private JPanel crearPanelEstado() {

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            panel.setOpaque(false);

            panel.add(rbDisponible);
            panel.add(rbOcupada);
            panel.add(rbMantenimiento);

            return panel;
        }

        private JPanel crearPanelAmenidades() {

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            panel.setOpaque(false);

            panel.add(chkAire);
            panel.add(chkWifi);
            panel.add(chkTelevision);
            panel.add(chkCajaFuerte);

            return panel;
        }

        private JPanel crearBotones() {

            JPanel botones = new JPanel(new GridLayout(1, 5, 8, 0));
            botones.setOpaque(false);

            JButton btnNuevo = EstiloHotel.crearBoton("Nuevo", EstiloHotel.VERDE, Color.WHITE);
            JButton btnGuardar = EstiloHotel.crearBoton("Guardar", EstiloHotel.DORADO, Color.WHITE);
            JButton btnEditar = EstiloHotel.crearBoton("Editar", EstiloHotel.CAFE, Color.WHITE);
            JButton btnEliminar = EstiloHotel.crearBoton("Eliminar", EstiloHotel.ROJO, Color.WHITE);
            JButton btnLimpiar = EstiloHotel.crearBotonBorde("Limpiar", EstiloHotel.BRONCE);

            btnNuevo.addActionListener(e -> prepararNueva());
            btnGuardar.addActionListener(e -> guardarHabitacion());
            btnEditar.addActionListener(e -> editarHabitacion());
            btnEliminar.addActionListener(e -> eliminarHabitacion());
            btnLimpiar.addActionListener(e -> limpiarFormulario());

            botones.add(btnNuevo);
            botones.add(btnGuardar);
            botones.add(btnEditar);
            botones.add(btnEliminar);
            botones.add(btnLimpiar);

            return botones;
        }

        private JPanel crearTarjetaTabla() {

            PanelRedondeado tarjeta = EstiloHotel.crearTarjeta(new BorderLayout(0, 14));

            JPanel arriba = new JPanel(new BorderLayout(20, 0));
            arriba.setOpaque(false);

            arriba.add(EstiloHotel.crearEncabezado(
                    "H A B I T A C I O N E S",
                    "Habitaciones registradas"), BorderLayout.CENTER);

            JPanel buscador = new JPanel(new BorderLayout(8, 0));
            buscador.setOpaque(false);
            buscador.setPreferredSize(new Dimension(290, 32));

            JLabel lblBuscar = new JLabel("Buscar:");
            lblBuscar.setFont(EstiloHotel.ETIQUETA);
            lblBuscar.setForeground(EstiloHotel.CAFE);

            buscador.add(lblBuscar, BorderLayout.WEST);
            buscador.add(txtBuscar, BorderLayout.CENTER);

            JPanel contenedorBuscador = new JPanel(new BorderLayout());
            contenedorBuscador.setOpaque(false);
            contenedorBuscador.add(buscador, BorderLayout.NORTH);

            arriba.add(contenedorBuscador, BorderLayout.EAST);

            tarjeta.add(arriba, BorderLayout.NORTH);

            EstiloHotel.configurarTabla(tabla);
            tabla.setRowSorter(ordenador);
            ajustarAnchoColumnas();

            tarjeta.add(EstiloHotel.crearScrollTabla(tabla), BorderLayout.CENTER);

            tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    cargarHabitacionSeleccionada();
                }
            });

            txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filtrarTabla();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filtrarTabla();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filtrarTabla();
                }
            });

            return tarjeta;
        }

        private void ajustarAnchoColumnas() {

            int[] anchos = {70, 90, 90, 80, 110, 220};

            for (int i = 0; i < anchos.length; i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            }
        }

        private void refrescarTabla() {

            modeloTabla.setRowCount(0);

            for (Habitacion habitacion : DatosHotel.getHabitaciones()) {
                modeloTabla.addRow(new Object[]{
                        habitacion.getNumero(),
                        habitacion.getTipo(),
                        String.format(Locale.US, "$%,.2f", habitacion.getPrecioPorNoche()),
                        habitacion.getCapacidad(),
                        habitacion.getEstado(),
                        habitacion.getAmenidades()
                });
            }

            ventana.actualizarTarjetas();
        }

        private void filtrarTabla() {

            String texto = txtBuscar.getText().trim();

            if (texto.isEmpty()) {
                ordenador.setRowFilter(null);
            } else {
                ordenador.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
            }
        }

        private void prepararNueva() {

            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                    "Puede registrar una habitacion nueva.",
                    "Nueva habitacion",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void guardarHabitacion() {

            if (!validarFormulario(null)) {
                return;
            }

            Habitacion habitacion = new Habitacion(
                    Integer.parseInt(txtNumero.getText().trim()),
                    (String) cmbTipo.getSelectedItem(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    (Integer) spCapacidad.getValue(),
                    getEstadoSeleccionado(),
                    chkAire.isSelected(),
                    chkWifi.isSelected(),
                    chkTelevision.isSelected(),
                    chkCajaFuerte.isSelected(),
                    txtDescripcion.getText().trim());

            DatosHotel.agregarHabitacion(habitacion);

            refrescarTabla();
            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                    "Habitacion guardada correctamente.",
                    "Guardar",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void editarHabitacion() {

            if (habitacionSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione en la tabla la habitacion que desea editar.",
                        "Editar",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validarFormulario(habitacionSeleccionada)) {
                return;
            }

            habitacionSeleccionada.setNumero(Integer.parseInt(txtNumero.getText().trim()));
            habitacionSeleccionada.setTipo((String) cmbTipo.getSelectedItem());
            habitacionSeleccionada.setPrecioPorNoche(Double.parseDouble(txtPrecio.getText().trim()));
            habitacionSeleccionada.setCapacidad((Integer) spCapacidad.getValue());
            habitacionSeleccionada.setEstado(getEstadoSeleccionado());
            habitacionSeleccionada.setAireAcondicionado(chkAire.isSelected());
            habitacionSeleccionada.setWifi(chkWifi.isSelected());
            habitacionSeleccionada.setTelevision(chkTelevision.isSelected());
            habitacionSeleccionada.setCajaFuerte(chkCajaFuerte.isSelected());
            habitacionSeleccionada.setDescripcion(txtDescripcion.getText().trim());

            refrescarTabla();
            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                    "Habitacion actualizada correctamente.",
                    "Editar",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void eliminarHabitacion() {

            if (habitacionSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione en la tabla la habitacion que desea eliminar.",
                        "Eliminar",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (Habitacion.OCUPADA.equals(habitacionSeleccionada.getEstado())) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar la habitacion "
                                + habitacionSeleccionada.getNumero() + " porque esta ocupada.",
                        "Eliminar",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Desea eliminar la habitacion "
                            + habitacionSeleccionada.getNumero() + "?",
                    "Confirmar eliminacion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {

                DatosHotel.eliminarHabitacion(habitacionSeleccionada);

                refrescarTabla();
                limpiarFormulario();

                JOptionPane.showMessageDialog(this,
                        "Habitacion eliminada correctamente.",
                        "Eliminar",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void limpiarFormulario() {

            txtNumero.setText("");
            txtPrecio.setText("");
            txtDescripcion.setText("");

            cmbTipo.setSelectedIndex(0);
            spCapacidad.setValue(2);

            rbDisponible.setSelected(true);

            chkAire.setSelected(false);
            chkWifi.setSelected(false);
            chkTelevision.setSelected(false);
            chkCajaFuerte.setSelected(false);

            habitacionSeleccionada = null;

            tabla.clearSelection();
            txtNumero.requestFocusInWindow();
        }

        private void cargarHabitacionSeleccionada() {

            int filaVista = tabla.getSelectedRow();

            if (filaVista == -1) {
                return;
            }

            int filaModelo = tabla.convertRowIndexToModel(filaVista);

            habitacionSeleccionada = DatosHotel.getHabitaciones().get(filaModelo);

            txtNumero.setText(String.valueOf(habitacionSeleccionada.getNumero()));
            txtPrecio.setText(String.format(Locale.US, "%.2f", habitacionSeleccionada.getPrecioPorNoche()));
            txtDescripcion.setText(habitacionSeleccionada.getDescripcion());

            cmbTipo.setSelectedItem(habitacionSeleccionada.getTipo());
            spCapacidad.setValue(habitacionSeleccionada.getCapacidad());

            if (Habitacion.OCUPADA.equals(habitacionSeleccionada.getEstado())) {
                rbOcupada.setSelected(true);
            } else if (Habitacion.MANTENIMIENTO.equals(habitacionSeleccionada.getEstado())) {
                rbMantenimiento.setSelected(true);
            } else {
                rbDisponible.setSelected(true);
            }

            chkAire.setSelected(habitacionSeleccionada.tieneAireAcondicionado());
            chkWifi.setSelected(habitacionSeleccionada.tieneWifi());
            chkTelevision.setSelected(habitacionSeleccionada.tieneTelevision());
            chkCajaFuerte.setSelected(habitacionSeleccionada.tieneCajaFuerte());
        }

        private boolean validarFormulario(Habitacion excepcion) {

            String numero = txtNumero.getText().trim();
            String precio = txtPrecio.getText().trim();

            if (numero.isEmpty() || precio.isEmpty()) {
                mostrarAdvertencia("El numero y el precio por noche son obligatorios.");
                return false;
            }

            if (!numero.matches("\\d+")) {
                mostrarAdvertencia("El numero de habitacion solo puede tener digitos.");
                txtNumero.requestFocusInWindow();
                return false;
            }

            if (DatosHotel.existeNumero(Integer.parseInt(numero), excepcion)) {
                mostrarAdvertencia("Ya existe una habitacion con el numero " + numero + ".");
                txtNumero.requestFocusInWindow();
                return false;
            }

            double valor;

            try {
                valor = Double.parseDouble(precio);
            } catch (NumberFormatException e) {
                mostrarAdvertencia("El precio por noche debe ser un numero.\nEjemplo: 125.50");
                txtPrecio.requestFocusInWindow();
                return false;
            }

            if (valor <= 0) {
                mostrarAdvertencia("El precio por noche debe ser mayor que cero.");
                txtPrecio.requestFocusInWindow();
                return false;
            }

            if ((Integer) spCapacidad.getValue() <= 0) {
                mostrarAdvertencia("La capacidad debe ser mayor que cero.");
                return false;
            }

            return true;
        }

        private String getEstadoSeleccionado() {

            if (rbOcupada.isSelected()) {
                return Habitacion.OCUPADA;
            }

            if (rbMantenimiento.isSelected()) {
                return Habitacion.MANTENIMIENTO;
            }

            return Habitacion.DISPONIBLE;
        }

        private void mostrarAdvertencia(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Validacion", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Parte de Luis

    private static class Huesped {

        private String identificacion;
        private String nombre;
        private String apellidos;
        private String telefono;
        private String correo;
        private String nacionalidad;
        private String observaciones;

        public Huesped(String identificacion, String nombre, String apellidos, String telefono,
                       String correo, String nacionalidad, String observaciones) {
            this.identificacion = identificacion;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.telefono = telefono;
            this.correo = correo;
            this.nacionalidad = nacionalidad;
            this.observaciones = observaciones;
        }

        public String getIdentificacion() {
            return identificacion;
        }

        public void setIdentificacion(String identificacion) {
            this.identificacion = identificacion;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getNacionalidad() {
            return nacionalidad;
        }

        public void setNacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public String getNombreCompleto() {
            return nombre + " " + apellidos;
        }

        @Override
        public String toString() {
            return identificacion + "  -  " + getNombreCompleto();
        }
    }

    private static class Reservacion {

        public static final String ACTIVA = "Activa";
        public static final String CANCELADA = "Cancelada";
        public static final String FINALIZADA = "Finalizada";

        public static final double PRECIO_DESAYUNO = 15.0;
        public static final double PRECIO_PARQUEO = 10.0;
        public static final double PRECIO_SPA = 40.0;

        private static final DateTimeFormatter FORMATO_FECHA =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        private int numero;
        private Huesped huesped;
        private Habitacion habitacion;
        private LocalDate fechaEntrada;
        private LocalDate fechaSalida;
        private int adultos;
        private int ninos;
        private int cantidadHabitaciones;
        private String estado;
        private boolean desayuno;
        private boolean parqueo;
        private boolean spa;

        public Reservacion(int numero, Huesped huesped, Habitacion habitacion,
                           LocalDate fechaEntrada, LocalDate fechaSalida,
                           int adultos, int ninos, int cantidadHabitaciones,
                           String estado, boolean desayuno, boolean parqueo, boolean spa) {
            this.numero = numero;
            this.huesped = huesped;
            this.habitacion = habitacion;
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.adultos = adultos;
            this.ninos = ninos;
            this.cantidadHabitaciones = cantidadHabitaciones;
            this.estado = estado;
            this.desayuno = desayuno;
            this.parqueo = parqueo;
            this.spa = spa;
        }

        public int getNumero() {
            return numero;
        }

        public Huesped getHuesped() {
            return huesped;
        }

        public void setHuesped(Huesped huesped) {
            this.huesped = huesped;
        }

        public Habitacion getHabitacion() {
            return habitacion;
        }

        public void setHabitacion(Habitacion habitacion) {
            this.habitacion = habitacion;
        }

        public LocalDate getFechaEntrada() {
            return fechaEntrada;
        }

        public void setFechaEntrada(LocalDate fechaEntrada) {
            this.fechaEntrada = fechaEntrada;
        }

        public LocalDate getFechaSalida() {
            return fechaSalida;
        }

        public void setFechaSalida(LocalDate fechaSalida) {
            this.fechaSalida = fechaSalida;
        }

        public int getAdultos() {
            return adultos;
        }

        public void setAdultos(int adultos) {
            this.adultos = adultos;
        }

        public int getNinos() {
            return ninos;
        }

        public void setNinos(int ninos) {
            this.ninos = ninos;
        }

        public int getCantidadHabitaciones() {
            return cantidadHabitaciones;
        }

        public void setCantidadHabitaciones(int cantidadHabitaciones) {
            this.cantidadHabitaciones = cantidadHabitaciones;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public boolean tieneDesayuno() {
            return desayuno;
        }

        public void setDesayuno(boolean desayuno) {
            this.desayuno = desayuno;
        }

        public boolean tieneParqueo() {
            return parqueo;
        }

        public void setParqueo(boolean parqueo) {
            this.parqueo = parqueo;
        }

        public boolean tieneSpa() {
            return spa;
        }

        public void setSpa(boolean spa) {
            this.spa = spa;
        }

        public int getNoches() {
            return (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        }

        public int getTotalPersonas() {
            return adultos + ninos;
        }

        public double getTotal() {
            return calcularTotal(habitacion, getNoches(), cantidadHabitaciones,
                    desayuno, parqueo, spa);
        }

        public static double calcularTotal(Habitacion habitacion, int noches, int cantidadHabitaciones,
                                           boolean desayuno, boolean parqueo, boolean spa) {

            if (habitacion == null || noches <= 0) {
                return 0;
            }

            double total = habitacion.getPrecioPorNoche() * noches * cantidadHabitaciones;

            if (desayuno) {
                total = total + PRECIO_DESAYUNO * noches;
            }

            if (parqueo) {
                total = total + PRECIO_PARQUEO * noches;
            }

            if (spa) {
                total = total + PRECIO_SPA;
            }

            return total;
        }

        public String getServicios() {

            String servicios = "";

            if (desayuno) {
                servicios = servicios + "Desayuno ";
            }

            if (parqueo) {
                servicios = servicios + "Parqueo ";
            }

            if (spa) {
                servicios = servicios + "Spa";
            }

            return servicios.trim().isEmpty() ? "Ninguno" : servicios.trim();
        }

        public String getFechaEntradaTexto() {
            return fechaEntrada.format(FORMATO_FECHA);
        }

        public String getFechaSalidaTexto() {
            return fechaSalida.format(FORMATO_FECHA);
        }

        public boolean estaActiva() {
            return ACTIVA.equals(estado);
        }
    }

    private static class DatosHotel {

        private static final List<Habitacion> HABITACIONES = new ArrayList<>();
        private static final List<Huesped> HUESPEDES = new ArrayList<>();
        private static final List<Reservacion> RESERVACIONES = new ArrayList<>();

        private static int siguienteNumeroReservacion = 1;

        static {
            cargarDatosDeEjemplo();
        }

        private DatosHotel() {
        }

        private static void cargarDatosDeEjemplo() {

            HABITACIONES.add(new Habitacion(101, "Standard", 85, 2, Habitacion.DISPONIBLE,
                    true, true, true, false, "Habitacion con vista al jardin."));
            HABITACIONES.add(new Habitacion(102, "Standard", 85, 2, Habitacion.DISPONIBLE,
                    true, true, true, false, "Habitacion con vista al jardin."));
            HABITACIONES.add(new Habitacion(103, "Standard", 90, 3, Habitacion.DISPONIBLE,
                    true, true, true, true, "Habitacion familiar con cama extra."));
            HABITACIONES.add(new Habitacion(201, "Deluxe", 130, 3, Habitacion.DISPONIBLE,
                    true, true, true, true, "Balcon privado con vista al volcan."));
            HABITACIONES.add(new Habitacion(202, "Deluxe", 130, 3, Habitacion.MANTENIMIENTO,
                    true, true, true, true, "En reparacion del aire acondicionado."));
            HABITACIONES.add(new Habitacion(203, "Deluxe", 145, 4, Habitacion.DISPONIBLE,
                    true, true, true, true, "Balcon privado y bano con tina."));
            HABITACIONES.add(new Habitacion(301, "Suite", 210, 4, Habitacion.DISPONIBLE,
                    true, true, true, true, "Suite con sala independiente."));
            HABITACIONES.add(new Habitacion(302, "Suite", 260, 6, Habitacion.DISPONIBLE,
                    true, true, true, true, "Suite presidencial con jacuzzi."));

            HUESPEDES.add(new Huesped("112340567", "Ana Lucia", "Soto Ramirez", "88881234",
                    "ana.soto@gmail.com", "Costa Rica", "Prefiere habitacion con vista al volcan."));
            HUESPEDES.add(new Huesped("205670890", "Carlos", "Lopez Ruiz", "87654321",
                    "carlos.lopez@gmail.com", "Nicaragua", ""));
            HUESPEDES.add(new Huesped("400123456", "Maria", "Garcia Perez", "83001122",
                    "maria.garcia@hotmail.com", "Estados Unidos", "Llega en vuelo nocturno."));

            Reservacion reservacion = new Reservacion(
                    siguienteNumeroReservacion,
                    HUESPEDES.get(0),
                    buscarHabitacion(102),
                    LocalDate.now(),
                    LocalDate.now().plusDays(3),
                    2, 0, 1,
                    Reservacion.ACTIVA,
                    true, false, false);

            siguienteNumeroReservacion++;

            RESERVACIONES.add(reservacion);
            buscarHabitacion(102).setEstado(Habitacion.OCUPADA);
        }

        public static List<Habitacion> getHabitaciones() {
            return HABITACIONES;
        }

        public static Habitacion buscarHabitacion(int numero) {

            for (Habitacion habitacion : HABITACIONES) {
                if (habitacion.getNumero() == numero) {
                    return habitacion;
                }
            }

            return null;
        }

        public static List<Habitacion> getHabitacionesDisponibles(String tipo, Habitacion incluirEsta) {

            List<Habitacion> disponibles = new ArrayList<>();

            for (Habitacion habitacion : HABITACIONES) {

                boolean sirveElTipo = "Todos".equals(tipo) || habitacion.getTipo().equals(tipo);
                boolean sePuedeUsar = habitacion.estaDisponible() || habitacion == incluirEsta;

                if (sirveElTipo && sePuedeUsar) {
                    disponibles.add(habitacion);
                }
            }

            return disponibles;
        }

        public static void agregarHabitacion(Habitacion habitacion) {
            HABITACIONES.add(habitacion);
        }

        public static void eliminarHabitacion(Habitacion habitacion) {
            HABITACIONES.remove(habitacion);
        }

        public static boolean existeNumero(int numero, Habitacion excepcion) {

            for (Habitacion habitacion : HABITACIONES) {
                if (habitacion != excepcion && habitacion.getNumero() == numero) {
                    return true;
                }
            }

            return false;
        }

        public static int contarHabitaciones(String estado) {

            int cantidad = 0;

            for (Habitacion habitacion : HABITACIONES) {
                if (habitacion.getEstado().equals(estado)) {
                    cantidad++;
                }
            }

            return cantidad;
        }

        public static List<Huesped> getHuespedes() {
            return HUESPEDES;
        }

        public static void agregarHuesped(Huesped huesped) {
            HUESPEDES.add(huesped);
        }

        public static void eliminarHuesped(Huesped huesped) {
            HUESPEDES.remove(huesped);
        }

        public static boolean existeIdentificacion(String identificacion, Huesped excepcion) {

            for (Huesped huesped : HUESPEDES) {
                if (huesped != excepcion && huesped.getIdentificacion().equals(identificacion)) {
                    return true;
                }
            }

            return false;
        }

        public static List<Reservacion> getReservaciones() {
            return RESERVACIONES;
        }

        public static void agregarReservacion(Reservacion reservacion) {
            RESERVACIONES.add(reservacion);
        }

        public static void eliminarReservacion(Reservacion reservacion) {
            RESERVACIONES.remove(reservacion);
        }

        public static int tomarNumeroReservacion() {
            int numero = siguienteNumeroReservacion;
            siguienteNumeroReservacion++;
            return numero;
        }

        public static int contarReservaciones(String estado) {

            int cantidad = 0;

            for (Reservacion reservacion : RESERVACIONES) {
                if (reservacion.getEstado().equals(estado)) {
                    cantidad++;
                }
            }

            return cantidad;
        }

        public static boolean tieneReservacionesActivas(Huesped huesped) {

            for (Reservacion reservacion : RESERVACIONES) {
                if (reservacion.getHuesped() == huesped && reservacion.estaActiva()) {
                    return true;
                }
            }

            return false;
        }

        public static double calcularIngresos() {

            double ingresos = 0;

            for (Reservacion reservacion : RESERVACIONES) {
                if (!Reservacion.CANCELADA.equals(reservacion.getEstado())) {
                    ingresos = ingresos + reservacion.getTotal();
                }
            }

            return ingresos;
        }
    }

    private static class PanelReportes extends JPanel {

        private final JLabel lblTotalHabitaciones = crearNumero();
        private final JLabel lblDisponibles = crearNumero();
        private final JLabel lblOcupadas = crearNumero();
        private final JLabel lblMantenimiento = crearNumero();
        private final JLabel lblOcupacion = crearNumero();
        private final JLabel lblHuespedes = crearNumero();
        private final JLabel lblActivas = crearNumero();
        private final JLabel lblIngresos = crearNumero();

        private final JLabel lblDetalleReservaciones = new JLabel();

        private final JTextArea txtNotas = new JTextArea();

        public PanelReportes() {

            setLayout(new BorderLayout(0, 18));
            setBackground(EstiloHotel.FONDO);
            setBorder(new EmptyBorder(20, 24, 20, 24));

            add(crearTarjetaResumen(), BorderLayout.CENTER);
            add(crearTarjetaNotas(), BorderLayout.SOUTH);

            actualizarReporte();
        }

        private JPanel crearTarjetaResumen() {

            PanelRedondeado tarjeta = EstiloHotel.crearTarjeta(new BorderLayout(0, 18));

            JPanel arriba = new JPanel(new BorderLayout(20, 0));
            arriba.setOpaque(false);

            arriba.add(EstiloHotel.crearEncabezadoCompacto(
                    "R E P O R T E S",
                    "Ocupacion e ingresos"), BorderLayout.CENTER);

            JButton btnActualizar = EstiloHotel.crearBotonBorde("Actualizar", EstiloHotel.BRONCE);
            btnActualizar.addActionListener(e -> {
                actualizarReporte();
                JOptionPane.showMessageDialog(this,
                        "Reporte actualizado.",
                        "Reportes",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            JPanel contenedorBoton = new JPanel(new BorderLayout());
            contenedorBoton.setOpaque(false);
            contenedorBoton.add(btnActualizar, BorderLayout.NORTH);

            arriba.add(contenedorBoton, BorderLayout.EAST);

            tarjeta.add(arriba, BorderLayout.NORTH);

            JPanel cuadros = new JPanel(new GridLayout(2, 4, 14, 12));
            cuadros.setOpaque(false);

            cuadros.add(crearCuadro("Habitaciones", lblTotalHabitaciones));
            cuadros.add(crearCuadro("Disponibles", lblDisponibles));
            cuadros.add(crearCuadro("Ocupadas", lblOcupadas));
            cuadros.add(crearCuadro("En mantenimiento", lblMantenimiento));
            cuadros.add(crearCuadro("Ocupacion", lblOcupacion));
            cuadros.add(crearCuadro("Huespedes", lblHuespedes));
            cuadros.add(crearCuadro("Reservaciones activas", lblActivas));
            cuadros.add(crearCuadro("Ingresos", lblIngresos));

            JPanel centro = new JPanel(new BorderLayout(0, 14));
            centro.setOpaque(false);
            centro.add(cuadros, BorderLayout.CENTER);

            lblDetalleReservaciones.setFont(EstiloHotel.PEQUENA);
            lblDetalleReservaciones.setForeground(EstiloHotel.TEXTO_SUAVE);

            centro.add(lblDetalleReservaciones, BorderLayout.SOUTH);

            tarjeta.add(centro, BorderLayout.CENTER);

            return tarjeta;
        }

        private JPanel crearCuadro(String titulo, JLabel numero) {

            PanelRedondeado cuadro = new PanelRedondeado(
                    new BorderLayout(0, 2), EstiloHotel.BEIGE_CLARO, EstiloHotel.BORDE, 10);
            cuadro.setBorder(new EmptyBorder(10, 16, 10, 16));

            JLabel lblTitulo = new JLabel(titulo.toUpperCase());
            lblTitulo.setFont(EstiloHotel.MINI);
            lblTitulo.setForeground(EstiloHotel.TEXTO_SUAVE);

            cuadro.add(lblTitulo, BorderLayout.NORTH);
            cuadro.add(numero, BorderLayout.CENTER);

            return cuadro;
        }

        private static JLabel crearNumero() {

            JLabel numero = new JLabel("0");
            numero.setFont(new Font("Georgia", Font.BOLD, 22));
            numero.setForeground(EstiloHotel.CAFE);

            return numero;
        }

        private JPanel crearTarjetaNotas() {

            PanelRedondeado tarjeta = EstiloHotel.crearTarjeta(new BorderLayout(0, 12));
            tarjeta.setPreferredSize(new Dimension(0, 205));

            tarjeta.add(EstiloHotel.crearEncabezadoCompacto(
                    "R E C E P C I O N",
                    "Notas rapidas"), BorderLayout.NORTH);

            txtNotas.setText("Recordar confirmar los transportes al aeropuerto.\n"
                    + "Revisar el estado de la habitacion 202 (mantenimiento).");

            tarjeta.add(EstiloHotel.crearAreaTexto(txtNotas, 3), BorderLayout.CENTER);

            JButton btnGuardarNota = EstiloHotel.crearBoton("Guardar nota",
                    EstiloHotel.DORADO, Color.WHITE);

            btnGuardarNota.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Nota guardada correctamente.",
                    "Notas rapidas",
                    JOptionPane.INFORMATION_MESSAGE));

            JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pie.setOpaque(false);
            pie.add(btnGuardarNota);

            tarjeta.add(pie, BorderLayout.SOUTH);

            return tarjeta;
        }

        public void actualizarReporte() {

            int totalHabitaciones = DatosHotel.getHabitaciones().size();
            int disponibles = DatosHotel.contarHabitaciones(Habitacion.DISPONIBLE);
            int ocupadas = DatosHotel.contarHabitaciones(Habitacion.OCUPADA);
            int mantenimiento = DatosHotel.contarHabitaciones(Habitacion.MANTENIMIENTO);

            lblTotalHabitaciones.setText(String.valueOf(totalHabitaciones));
            lblDisponibles.setText(String.valueOf(disponibles));
            lblOcupadas.setText(String.valueOf(ocupadas));
            lblMantenimiento.setText(String.valueOf(mantenimiento));

            int porcentaje = totalHabitaciones == 0
                    ? 0
                    : (int) Math.round(ocupadas * 100.0 / totalHabitaciones);

            lblOcupacion.setText(porcentaje + "%");

            lblHuespedes.setText(String.valueOf(DatosHotel.getHuespedes().size()));

            int activas = DatosHotel.contarReservaciones(Reservacion.ACTIVA);
            int canceladas = DatosHotel.contarReservaciones(Reservacion.CANCELADA);
            int finalizadas = DatosHotel.contarReservaciones(Reservacion.FINALIZADA);

            lblActivas.setText(String.valueOf(activas));
            lblIngresos.setText(String.format("$%,.0f", DatosHotel.calcularIngresos()));

            lblDetalleReservaciones.setText("Reservaciones:  " + activas + " activas  ·  "
                    + canceladas + " canceladas  ·  " + finalizadas + " finalizadas  ·  "
                    + DatosHotel.getReservaciones().size() + " en total."
                    + "     Los ingresos no incluyen las reservaciones canceladas.");
        }
    }
}
