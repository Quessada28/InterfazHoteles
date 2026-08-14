# InterfazHoteles — Sistema de Gestión Hotelera Tabacon

Proyecto de Java Swing (IntelliJ IDEA) del sistema **Tabacon Hotel**.

Contiene la parte de Kevin (ventana principal, portada con imagen, navegación inferior,
reloj y barra de estado) y la de Luis (módulos de Huéspedes, Reservaciones y Reportes),
ya integradas y funcionando.

> Los archivos de Luis van sin tildes en los textos de la interfaz; DashboardSwing
> conserva las suyas.

---

## 1. Organización del código

Todo el sistema vive en `src/DashboardSwing.java`, dividido con los dos marcadores de la
base del profe: primero **la parte de Kevin** y después **la parte de Luis**, donde están
las clases de los módulos como clases anidadas.

| Clase | Para qué sirve | Dónde |
|---|---|---|
| `DashboardSwing` | Ventana principal, navegación con `CardLayout`, reloj, barra de estado y **las 4 tarjetas indicadoras**. | Kevin 1 y 3 |
| `PanelPortada` | Dibuja la imagen de la portada con el velo oscuro. | Kevin 1 |
| `PanelRedondeado` / `BotonRedondeado` | Paneles y botones con esquinas redondeadas. | Kevin 2 |
| `EstiloHotel` | Colores, tipografías y los métodos que crean los componentes. Lo usan los dos módulos. | Kevin 2 |
| `Habitacion` | Una habitación: número, tipo, precio, capacidad, estado, amenidades y descripción. | Kevin 4 |
| `PanelHabitaciones` | CRUD completo de habitaciones. | Kevin 4 |
| `Huesped` | Un huésped con sus siete datos. | Luis 1 |
| `Reservacion` | Una reservación; **calcula noches y total**. | Luis 2 |
| `DatosHotel` | Las tres listas en memoria y las consultas. Es la "base de datos". | Luis |
| `PanelHuespedes` | CRUD de huéspedes. | Luis 1 |
| `PanelReservaciones` | CRUD de reservaciones, filtros y validaciones cruzadas. | Luis 2, 3 y 4 |
| `PanelReportes` | Ocupación, ingresos y notas rápidas. | Luis 5 |

Los archivos sueltos son solo `Main.java` (arranca la aplicación) y `MySimpleGUI.java`
(el ejemplo original del profe, que no se usa).

`DashboardSwing` crea los paneles de Luis y los mete en el `CardLayout`:

```java
panelHuespedes = new PanelHuespedes();
panelReservaciones = new PanelReservaciones();
panelReportes = new PanelReportes();

contenedorPaneles.add(panelHuespedes, "HUESPEDES");
contenedorPaneles.add(panelReservaciones, "RESERVACIONES");
contenedorPaneles.add(panelReportes, "REPORTES");
```

---

## 2. Cómo se comunican los módulos

El problema: el combo de huéspedes del módulo de Reservaciones tiene que mostrar los
huéspedes que se registraron en el **otro** módulo.

La solución es `DatosHotel`, una clase con listas **estáticas**. Los dos paneles leen y
escriben en las mismas listas, así que no hay copias desincronizadas:

```
PanelHuespedes  ──escribe──►  DatosHotel.HUESPEDES  ──lee──►  PanelReservaciones
PanelReservaciones ──escribe──► DatosHotel.RESERVACIONES ──lee──► PanelReportes
                    ──cambia estado──► DatosHotel.HABITACIONES
```

Cuando se entra a una sección, `DashboardSwing.mostrarSeccion()` avisa al panel para que
se refresque:

```java
if (clave.equals("RESERVACIONES")) {
    panelReservaciones.actualizarListas();
}
if (clave.equals("REPORTES")) {
    panelReportes.actualizarReporte();
}
```

---

## 3. Módulo de Huéspedes (punto 1)

**Formulario** en dos columnas: identificación, nombre, apellidos, teléfono, correo,
nacionalidad (`JComboBox`) y observaciones (`JTextArea`).
**Tabla** con los registros y **buscador dinámico** arriba a la derecha.

### Los cinco botones

| Botón | Qué hace |
|---|---|
| Nuevo | Limpia el formulario para registrar otro huésped. |
| Guardar | Valida y **agrega** un huésped nuevo a la lista. |
| Editar | Valida y **modifica** el huésped seleccionado en la tabla. |
| Eliminar | Pide confirmación y lo borra. |
| Limpiar | Vacía los campos y quita la selección. |

### Validaciones (método `validarFormulario`)

1. Identificación, nombre, apellidos, teléfono y correo son obligatorios.
2. La identificación **solo acepta números**: `identificacion.matches("\\d+")`.
3. La identificación **no se puede repetir**: `DatosHotel.existeIdentificacion(...)`.
4. El teléfono debe tener **8 dígitos**: `telefono.matches("\\d{8}")`.
5. El correo debe cumplir el patrón `^[\w.+-]+@[\w-]+\.[A-Za-z]{2,}$`.

El truco del parámetro `excepcion`: al **editar**, la propia identificación del huésped no
debe contar como repetida, entonces se pasa el objeto que se está editando para saltárselo.

### Regla extra

No se puede eliminar un huésped que tenga reservaciones activas; si se intenta, sale un
mensaje de error. Así la tabla de reservaciones nunca queda apuntando a un huésped borrado.

---

## 4. Módulo de Reservaciones (punto 2)

Es el "Book Room" del diseño, pero completo.

### El formulario

| Campo | Componente | Detalle |
|---|---|---|
| Huésped | `JComboBox<Huesped>` | Se llena con la lista de `DatosHotel`. |
| Tipo de habitación | `JComboBox<String>` | Filtra el combo de habitaciones. |
| Habitación | `JComboBox<Habitacion>` | **Solo las disponibles** del tipo elegido. |
| Cantidad | `JSpinner` | De 1 a 5 habitaciones. |
| Fecha de entrada / salida | `JSpinner` con `SpinnerDateModel` | Formato `dd/MM/yyyy`. |
| Adultos / Niños | `JSpinner` | Adultos desde 1, niños desde 0. |
| Estado | `JRadioButton` en `ButtonGroup` | Activa / Cancelada / Finalizada. |
| Servicios | `JCheckBox` | Desayuno, parqueo, spa. |

### Cálculo automático del total

La fórmula vive en `Reservacion.calcularTotal(...)`:

```
total = precio de la habitacion x noches x cantidad de habitaciones
      + desayuno ($15 por noche)
      + parqueo  ($10 por noche)
      + spa      ($40 una sola vez)
```

Se recalcula **sola** porque está enganchada a los eventos: `ChangeListener` de los
spinner, `ActionListener` de las casillas y del combo de habitaciones. Cada vez que algo
cambia se llama `actualizarTotal()` y se reescribe la etiqueta grande del recuadro beige.

### El botón Cancelar reserva

Distinto de Eliminar: **no borra el registro**, le pone estado `Cancelada` y **libera la
habitación** (vuelve a `Disponible`), previa confirmación con `JOptionPane`.

---

## 5. Búsqueda y filtros (punto 3)

Las dos tablas usan `TableRowSorter` con `RowFilter`.

- **Buscador dinámico**: un `DocumentListener` filtra con cada tecla.
  `Pattern.quote(texto)` evita que un punto o un signo se interprete como expresión
  regular, y `(?i)` hace que no importen las mayúsculas.
- **Filtro por estado** y **filtro por tipo de habitación**: dos `JComboBox` con
  `ItemListener`, que filtran por una columna específica.

Los tres criterios se combinan con `RowFilter.andFilter(...)`, así que se pueden usar al
mismo tiempo (por ejemplo: buscar "Ana" + estado Activa + tipo Standard).

---

## 6. Validaciones cruzadas (punto 4)

Están en `PanelReservaciones.validarFormulario(...)`, en este orden:

1. Tiene que haber un **huésped** seleccionado (si no hay ninguno registrado, avisa).
2. Tiene que haber una **habitación** seleccionada.
3. **La fecha de salida debe ser posterior a la de entrada** (si no, las noches dan 0 o negativo).
4. **No se puede reservar una habitación ocupada o en mantenimiento**; la excepción es la
   habitación que ya tenía la reservación que se está editando.
5. **La cantidad de personas no puede exceder la capacidad**:
   `adultos + niños <= capacidad x cantidad de habitaciones`.

Además, `Eliminar` y `Cancelar` siempre piden confirmación con `JOptionPane`.

### Estados de las habitaciones

`actualizarEstadoHabitacion(...)` mantiene todo cuadrado: al guardar una reservación
activa la habitación pasa a `Ocupada`; si se cambia de habitación, la anterior se libera;
si la reservación se cancela o finaliza, la habitación vuelve a `Disponible`.
Una habitación en `Mantenimiento` **nunca** se libera automáticamente.

---

## 7. Panel de Reportes (punto 5)

Ocho cuadros calculados en el momento: habitaciones totales, disponibles, ocupadas, en
mantenimiento, porcentaje de ocupación, huéspedes registrados, reservaciones activas e
ingresos. Debajo, el detalle de reservaciones por estado y el panel de **notas rápidas**.

Los ingresos **no incluyen** las reservaciones canceladas.

---

## 8. Los eventos de Luis (para la defensa del trabajo)

| Evento | Dónde está | Qué hace |
|---|---|---|
| `DocumentListener` | buscador de las dos tablas | Filtra mientras se escribe. |
| `ChangeListener` | `JSpinner` de fechas, personas y cantidad | Recalcula el total. |
| `ItemListener` | combo de tipo y combos de filtro | Filtra habitaciones y tabla. |
| `ListSelectionListener` | las dos tablas | Carga la fila en el formulario. |
| `ActionListener` | los botones del CRUD | Guardar, editar, eliminar, cancelar... |
| `ActionListener` | `JCheckBox` de servicios | Recalcula el total. |

Son 6 tipos de eventos distintos, más los 5 de Kevin en la ventana principal.

---

## 9. Cómo ejecutarlo

- **IntelliJ IDEA**: abrir el proyecto y ejecutar `Main`.
  Si aparece "No SDK", asignarlo en *File > Project Structure > Project SDK*.
- **Consola** (desde la carpeta del proyecto, para que encuentre la imagen):

```bash
javac -encoding UTF-8 -d out/production/Clase11AgostoQuiz src/*.java
java -cp out/production/Clase11AgostoQuiz Main
```

### Datos de ejemplo

Al abrir ya hay 8 habitaciones (una ocupada y una en mantenimiento), 3 huéspedes y 1
reservación activa, para poder probar el flujo completo sin tener que digitar nada.
Están en `DatosHotel.cargarDatosDeEjemplo()`.

---

## 10. Cómo quedaron unidas las dos partes

Las dos partes comparten tres piezas, por eso al unirlas quedó **una sola** de cada una:

- **`EstiloHotel`** — cada quien tenía la suya. Quedó una sola en la parte de Kevin (el
  documento dice que la clase de estilo la dueña una sola persona), con los métodos de
  ambas versiones.
- **`Habitacion`** — la de Luis solo leía (número, tipo, precio, capacidad, estado); la de
  Kevin además guarda amenidades, descripción y los `set...` del CRUD. Quedó la de Kevin,
  conservando el `toString` de la de Luis porque es lo que se ve en el `JComboBox` de
  habitaciones del módulo de Reservaciones.
- **`DatosHotel`** — quedó la de Luis (habitaciones, huéspedes y reservaciones) más los
  métodos que necesita el CRUD de Kevin: `agregarHabitacion`, `eliminarHabitacion` y
  `existeNumero`.

Gracias a eso las **4 tarjetas del punto 3 ya muestran datos reales de los dos módulos**:
las habitaciones disponibles y ocupadas salen del módulo de Kevin, y las reservaciones
activas y los ingresos salen del de Luis (`contarReservaciones` y `calcularIngresos`).

Y funciona en las dos direcciones: si se registra una habitación en el módulo de Kevin,
aparece en el combo de habitaciones de Reservaciones; y si se ocupa o cancela una
reservación en el módulo de Luis, cambian los números de las tarjetas de Inicio.
