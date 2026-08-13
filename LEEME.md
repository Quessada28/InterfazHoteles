# ParteKevin — Sistema de Gestión Hotelera "Royelle"

Proyecto de Java Swing (IntelliJ IDEA). Tema elegido: **Gestión de Hotel**.

## Qué está hecho: Punto 1 — Ventana principal y navegación

El código conserva la misma cabecera de imports de la base del profe y los marcadores
`// Parte de Kevin` y `// Parte de Luis`, para poder copiarlo y pegarlo directamente.

- `src/Main.java` — clase de arranque, crea la ventana en el hilo de eventos.
- `src/DashboardSwing.java` — ventana principal:
  - `JFrame` con `BorderLayout`, tamaño 1500x930, tamaño mínimo y Look & Feel del sistema.
  - Barra superior de contacto (teléfono, correo y dirección del hotel).
  - **Portada con imagen** (`imagenes/portada.jpg`) y el título de la sección encima.
  - **Barra de navegación en la parte inferior** con: Inicio, Habitaciones, Huéspedes,
    Reservaciones, Reportes, Acerca de y Cerrar sesión.
  - **Navegación real con `CardLayout`**: cada botón muestra su propio panel (ya no un `JOptionPane`).
  - Barra de sección con el subtítulo y el reloj (`Timer`).
  - Barra de estado inferior que indica la sección activa.

### Diseño

La interfaz está inspirada en el sitio del hotel Tabacón: café `#483627`, bronce `#AE874C`,
dorado `#BD955A`, beige `#D8D1C9` y fondo crema `#FAF8F5`; títulos en **Georgia** (sustituto
del serif *Libre Caslon* del sitio) y textos en **Segoe UI**. Al final del archivo hay tres
clases internas que hacen el trabajo gráfico:

- `PanelPortada` — dibuja la imagen de portada recortada a lo ancho y le aplica un velo oscuro.
- `PanelRedondeado` — tarjetas y paneles con esquinas redondeadas, borde y degradado opcional.
- `BotonRedondeado` — los botones tipo píldora de la barra inferior.

### La imagen de portada

Está en `imagenes/portada.jpg` y se carga con `ImageIcon` probando varias rutas; si no
encuentra el archivo, la portada se dibuja con un degradado y la aplicación funciona igual.
Para cambiar la foto basta con reemplazar ese archivo por otro con el mismo nombre.

### Eventos ya implementados en este punto

1. Clic en los botones del menú → cambio de panel con `CardLayout`.
2. Hover del menú con `MouseAdapter` → resalta la opción bajo el mouse.
3. `Timer` del reloj → refresca la fecha y hora cada segundo.
4. Botón Cerrar sesión → `JOptionPane` de confirmación.
5. Cierre de la ventana (botón X) → `WindowAdapter` con confirmación de salida.

## Qué falta (siguientes puntos)

Cada sección muestra por ahora un panel provisional que dice qué contenido llevará:

| Sección | Contenido pendiente | Responsable |
|---|---|---|
| Inicio | Las 4 tarjetas indicadoras (punto 3) | Kevin |
| Habitaciones | Formulario + JTable + CRUD + buscador (punto 4) | Kevin |
| Huéspedes | Formulario, tabla y CRUD | Luis |
| Reservaciones | Formulario, tabla y CRUD | Luis |

El punto 2 (clase de estilo compartida con `crearBoton`, `crearPanelRedondeado`,
`crearCampoTexto`, `configurarCombo`, `agregarFilaFormulario` y `crearTarjeta`) sale de la
paleta y de las tipografías que ya están declaradas al inicio de `DashboardSwing`.

### Dónde se conectan los módulos

En el método `crearContenidoPrincipal()` se registran las tarjetas del `CardLayout`.
Para agregar un módulo solo hay que reemplazar la llamada a `crearPanelPendiente(...)`
por el panel real, dejando la misma clave, por ejemplo:

```java
contenedorPaneles.add(crearPanelHabitaciones(), "HABITACIONES");
```

## Cómo ejecutarlo

- **En IntelliJ IDEA**: abrir la carpeta `ParteKevin` como proyecto y ejecutar `Main`.
  Si aparece "No SDK", asignarlo en *File > Project Structure > Project SDK*.
- **Por consola**:

```bash
javac -encoding UTF-8 -d out/production/ParteKevin src/*.java
java -cp out/production/ParteKevin Main
```

## Nota sobre los iconos y los colores

- Los símbolos de la navegación y de los servicios se escriben con la fuente
  **Segoe UI Symbol** (método `textoConIcono`), porque la fuente normal no los incluye y se
  verían como cuadros. Ese mismo método les pone el color dorado con HTML.
- Los botones se dibujan con `BotonRedondeado` porque el Look & Feel de Windows pinta su
  propio fondo e ignora `setBackground`.
- Al ejecutar por consola hay que hacerlo desde la carpeta `ParteKevin` para que encuentre
  `imagenes/portada.jpg`. En IntelliJ funciona directo porque usa esa carpeta como
  directorio de trabajo.
