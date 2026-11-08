package com.mycompany.cine.controladores;

import com.mycompany.cine.App;
import com.mycompany.cine.modelo.Administrador;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * Controlador para la vista 'administradores.fxml' cumpliendo con la
 * especificación de la Actividad 3.2: Validación de Vistas.
 * 
 * Gestiona en tiempo real las comprobaciones de campos, mensajes dinámicos
 * debajo de cada control, validación completa con Alert, y ciclo de vida de los botones:
 * - Nuevo y Validar habilitados al inicio.
 * - Guardar deshabilitado hasta que todo el formulario sea válido.
 * - Editar y Eliminar habilitados únicamente cuando hay una fila seleccionada.
 * - Sin conexión directa a BD: simulación en memoria con las columnas exactas
 *   de la tabla MySQL 'administradores'.
 */
public class AdministradoresController implements Initializable {

    // ==========================================
    // CONTROLES DE ENTRADA (COLUMNAS BD)
    // ==========================================
    @FXML private TextField txtId;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cmbRol;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private ImageView imgFotoPreview;
    @FXML private Label lblRutaImagen;
    @FXML private TextField txtCreado;
    @FXML private TextField txtEditado;

    // ==========================================
    // LABELS DE VALIDACIÓN DEBAJO DE LOS CONTROLES
    // ==========================================
    @FXML private Label lblErrorId;
    @FXML private Label lblErrorUsuario;
    @FXML private Label lblErrorNombres;
    @FXML private Label lblErrorApellidos;
    @FXML private Label lblErrorCorreo;
    @FXML private Label lblErrorContrasena;
    @FXML private Label lblErrorRol;
    @FXML private Label lblErrorEstado;

    // ==========================================
    // BOTONES DE ACCIÓN
    // ==========================================
    @FXML private Button btnNuevo;
    @FXML private Button btnValidar;
    @FXML private Button btnGuardar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    // ==========================================
    // TABLA Y COLUMNAS (11 COLUMNAS DE LA BD)
    // ==========================================
    @FXML private TableView<Administrador> tblAdministradores;
    @FXML private TableColumn<Administrador, Integer> colId;
    @FXML private TableColumn<Administrador, String> colUsuario;
    @FXML private TableColumn<Administrador, String> colNombres;
    @FXML private TableColumn<Administrador, String> colApellidos;
    @FXML private TableColumn<Administrador, String> colCorreo;
    @FXML private TableColumn<Administrador, String> colContrasena;
    @FXML private TableColumn<Administrador, String> colRol;
    @FXML private TableColumn<Administrador, String> colRutaImagen;
    @FXML private TableColumn<Administrador, String> colEstado;
    @FXML private TableColumn<Administrador, String> colCreado;
    @FXML private TableColumn<Administrador, String> colEditado;

    // Colección observable para la tabla en memoria
    private final ObservableList<Administrador> listaAdministradores = FXCollections.observableArrayList();
    private String rutaImagenSeleccionada = null;

    // Patrones de validación
    private static final Pattern PATRON_SOLO_LETRAS = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$");
    private static final Pattern PATRON_USUARIO = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Bandera para evitar disparar validaciones de error mientras se limpia
    private boolean limpiandoFormulario = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarCombos();
        configurarColumnasTabla();
        cargarDatosDePrueba();
        configurarValidacionesTiempoReal();
        configurarSeleccionTabla();
        restablecerEstadoInicialBotones();
        ocultarTodosLosMensajesDeError();
    }

    // =========================================================================
    // CONFIGURACIÓN DE INTERFAZ Y COMPONENTES
    // =========================================================================
    private void configurarCombos() {
        cmbRol.setItems(FXCollections.observableArrayList(
                "SuperAdmin", "Administrador General", "Gerente de Turno", "Supervisor de Área"
        ));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Activo", "Inactivo", "Suspendido"
        ));
    }

    private void configurarColumnasTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        // Enmascarar contraseña en la tabla por seguridad
        colContrasena.setCellValueFactory(cellData -> {
            String pass = cellData.getValue().getContrasena();
            if (pass == null || pass.isEmpty()) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty("••••••••");
        });
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colRutaImagen.setCellValueFactory(new PropertyValueFactory<>("rutaImagen"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCreado.setCellValueFactory(new PropertyValueFactory<>("creado"));
        colEditado.setCellValueFactory(new PropertyValueFactory<>("editado"));

        tblAdministradores.setItems(listaAdministradores);
    }

    private void cargarDatosDePrueba() {
        // Datos iniciales en memoria para probar las columnas sin conexión a BD directa
        String ahora = LocalDateTime.now().format(FORMATO_FECHA_HORA);
        Administrador admin1 = new Administrador(
                1, "admin_cine", "Carlos Alberto", "Gómez Pérez", "carlos.gomez@cine.com",
                "admin1234", "SuperAdmin", "sin_imagen.png", "Activo", ahora, ahora
        );
        Administrador admin2 = new Administrador(
                2, "marian_sup", "Mariana", "Torres Ruiz", "mariana.torres@cine.com",
                "claveSegura99", "Supervisor de Área", "avatar2.png", "Activo", ahora, ahora
        );
        listaAdministradores.addAll(admin1, admin2);
    }

    // =========================================================================
    // 1. IMPLEMENTACIÓN DE VALIDACIONES EN TIEMPO REAL (ESPECIFICACIÓN 3.2)
    // =========================================================================
    private void configurarValidacionesTiempoReal() {
        // ID: enteros positivos
        txtId.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarId(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Usuario: alfanumérico sin espacios de 3 a 20 caracteres
        txtUsuario.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarUsuario(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Nombres: solo letras y espacios
        txtNombres.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarNombres(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Apellidos: solo letras y espacios
        txtApellidos.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarApellidos(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Correo: formato válido con @ y dominio
        txtCorreo.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarCorreo(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Contraseña: mínimo 6 caracteres
        txtContrasena.textProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarContrasena(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Rol: ComboBox obligatorio
        cmbRol.valueProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarRol(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });

        // Estado: ComboBox obligatorio
        cmbEstado.valueProperty().addListener((obs, viejo, nuevo) -> {
            if (!limpiandoFormulario) {
                validarEstado(nuevo);
                actualizarEstadoBotonGuardar();
            }
        });
    }

    // =========================================================================
    // 2. MENSAJES DE ERROR DEBAJO DE LOS CONTROLES (LABELS DINÁMICOS)
    // =========================================================================
    public boolean validarId(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorId, "Este campo es obligatorio.");
            return false;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor <= 0) {
                mostrarError(lblErrorId, "Debe ser un número entero mayor que 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError(lblErrorId, "Formato incorrecto: debe ser un número entero.");
            return false;
        }
        ocultarError(lblErrorId);
        return true;
    }

    public boolean validarUsuario(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorUsuario, "Este campo es obligatorio.");
            return false;
        }
        if (!PATRON_USUARIO.matcher(texto.trim()).matches()) {
            mostrarError(lblErrorUsuario, "Debe tener de 3 a 20 caracteres (letras, números o _).");
            return false;
        }
        ocultarError(lblErrorUsuario);
        return true;
    }

    public boolean validarNombres(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorNombres, "Este campo es obligatorio.");
            return false;
        }
        if (!PATRON_SOLO_LETRAS.matcher(texto.trim()).matches()) {
            mostrarError(lblErrorNombres, "Solo se admiten letras y espacios.");
            return false;
        }
        ocultarError(lblErrorNombres);
        return true;
    }

    public boolean validarApellidos(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorApellidos, "Este campo es obligatorio.");
            return false;
        }
        if (!PATRON_SOLO_LETRAS.matcher(texto.trim()).matches()) {
            mostrarError(lblErrorApellidos, "Solo se admiten letras y espacios.");
            return false;
        }
        ocultarError(lblErrorApellidos);
        return true;
    }

    public boolean validarCorreo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorCorreo, "Este campo es obligatorio.");
            return false;
        }
        if (!PATRON_CORREO.matcher(texto.trim()).matches()) {
            mostrarError(lblErrorCorreo, "Formato de correo no válido (ej. usuario@dominio.com).");
            return false;
        }
        ocultarError(lblErrorCorreo);
        return true;
    }

    public boolean validarContrasena(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarError(lblErrorContrasena, "Este campo es obligatorio.");
            return false;
        }
        if (texto.trim().length() < 6) {
            mostrarError(lblErrorContrasena, "La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        ocultarError(lblErrorContrasena);
        return true;
    }

    public boolean validarRol(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            mostrarError(lblErrorRol, "Este campo es obligatorio.");
            return false;
        }
        ocultarError(lblErrorRol);
        return true;
    }

    public boolean validarEstado(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            mostrarError(lblErrorEstado, "Este campo es obligatorio.");
            return false;
        }
        ocultarError(lblErrorEstado);
        return true;
    }

    private void mostrarError(Label label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
        label.setManaged(true);
    }

    private void ocultarError(Label label) {
        label.setText("");
        label.setVisible(false);
        label.setManaged(false);
    }

    private void ocultarTodosLosMensajesDeError() {
        ocultarError(lblErrorId);
        ocultarError(lblErrorUsuario);
        ocultarError(lblErrorNombres);
        ocultarError(lblErrorApellidos);
        ocultarError(lblErrorCorreo);
        ocultarError(lblErrorContrasena);
        ocultarError(lblErrorRol);
        ocultarError(lblErrorEstado);
    }

    // =========================================================================
    // 3. VALIDACIÓN COMPLETA DEL FORMULARIO Y ESTADO DE GUARDAR
    // =========================================================================
    public boolean esFormularioValido() {
        boolean idValido = validarId(txtId.getText());
        boolean usuarioValido = validarUsuario(txtUsuario.getText());
        boolean nombresValido = validarNombres(txtNombres.getText());
        boolean apellidosValido = validarApellidos(txtApellidos.getText());
        boolean correoValido = validarCorreo(txtCorreo.getText());
        boolean contrasenaValida = validarContrasena(txtContrasena.getText());
        boolean rolValido = validarRol(cmbRol.getValue());
        boolean estadoValido = validarEstado(cmbEstado.getValue());

        return idValido && usuarioValido && nombresValido && apellidosValido
                && correoValido && contrasenaValida && rolValido && estadoValido;
    }

    private void actualizarEstadoBotonGuardar() {
        // En tiempo real: si todos los campos son válidos se habilita Guardar, si no se deshabilita
        boolean valido = esFormularioValido();
        btnGuardar.setDisable(!valido);
    }

    // =========================================================================
    // 4. HABILITACIÓN Y DESHABILITACIÓN DE BOTONES
    // =========================================================================
    private void restablecerEstadoInicialBotones() {
        // "Al iniciar cada formulario, los botones Nuevo y Validar deberán estar habilitados,
        // mientras que Guardar, Editar y Eliminar deberán permanecer deshabilitados"
        btnNuevo.setDisable(false);
        btnValidar.setDisable(false);
        btnGuardar.setDisable(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private void configurarSeleccionTabla() {
        // "Los botones Editar y Eliminar deberán habilitarse únicamente cuando exista
        // un registro seleccionado en la tabla"
        tblAdministradores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, admin) -> {
            if (admin != null) {
                cargarAdministradorEnFormulario(admin);
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnGuardar.setDisable(true); // En modo edición no se guarda duplicado
            } else {
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
            }
        });
    }

    private void cargarAdministradorEnFormulario(Administrador admin) {
        limpiandoFormulario = true;
        txtId.setText(String.valueOf(admin.getId()));
        txtUsuario.setText(admin.getUsuario() != null ? admin.getUsuario() : "");
        txtNombres.setText(admin.getNombres());
        txtApellidos.setText(admin.getApellidos());
        txtCorreo.setText(admin.getCorreo());
        txtContrasena.setText(admin.getContrasena() != null ? admin.getContrasena() : "");
        cmbRol.setValue(admin.getRol());
        cmbEstado.setValue(admin.getEstado());
        txtCreado.setText(admin.getCreado() != null ? admin.getCreado() : "");
        txtEditado.setText(admin.getEditado() != null ? admin.getEditado() : "");

        rutaImagenSeleccionada = admin.getRutaImagen();
        lblRutaImagen.setText(rutaImagenSeleccionada != null ? rutaImagenSeleccionada : "Sin imagen seleccionada");
        cargarPreviewImagen(rutaImagenSeleccionada);

        ocultarTodosLosMensajesDeError();
        limpiandoFormulario = false;
    }

    // =========================================================================
    // ACCIONES DE BOTONES (NUEVO, VALIDAR, GUARDAR, EDITAR, ELIMINAR, LIMPIAR)
    // =========================================================================
    @FXML
    private void handleNuevo(ActionEvent event) {
        limpiarFormularioCompleto();
        restablecerEstadoInicialBotones();
        txtId.requestFocus();
    }

    @FXML
    private void handleValidar(ActionEvent event) {
        // Especificación 3.2:
        // "El botón Validar deberá comprobar todos los campos y mostrar una alerta
        // cuando la información sea correcta. Si existen errores, se deberán mostrar
        // los mensajes debajo de los controles correspondientes."
        boolean valido = esFormularioValido();
        if (valido) {
            mostrarAlerta(
                    AlertType.INFORMATION,
                    "Validación Exitosa",
                    "Comprobación de Campos Completa",
                    "Todos los datos cumplen las validaciones correctamente."
            );
        } else {
            mostrarAlerta(
                    AlertType.WARNING,
                    "Errores en el Formulario",
                    "Campos incompletos o con formato incorrecto",
                    "Por favor revisa los mensajes de error mostrados debajo de cada campo."
            );
        }
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        if (!esFormularioValido()) {
            mostrarAlerta(AlertType.ERROR, "Error de Validación", "Datos no válidos", "El formulario contiene errores que deben ser corregidos.");
            return;
        }

        int id = Integer.parseInt(txtId.getText().trim());

        // Comprobar ID duplicado
        for (Administrador a : listaAdministradores) {
            if (a.getId() == id) {
                mostrarError(lblErrorId, "Ya existe un registro con este ID.");
                mostrarAlerta(AlertType.ERROR, "ID Duplicado", "Identificador no disponible", "Ya existe un administrador con el ID #" + id);
                return;
            }
        }

        String timestamp = LocalDateTime.now().format(FORMATO_FECHA_HORA);
        Administrador nuevo = new Administrador(
                id,
                txtUsuario.getText().trim(),
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                txtContrasena.getText().trim(),
                cmbRol.getValue(),
                rutaImagenSeleccionada != null ? rutaImagenSeleccionada : "default_user.png",
                cmbEstado.getValue(),
                timestamp,
                timestamp
        );

        listaAdministradores.add(nuevo);
        mostrarAlerta(AlertType.INFORMATION, "Registro Exitoso", "Administrador Guardado", "El administrador fue registrado en la lista con éxito.");
        handleLimpiar(null);
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Administrador seleccionado = tblAdministradores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin Selección", "Aviso", "Selecciona un registro de la tabla para editar.");
            return;
        }

        if (!esFormularioValido()) {
            mostrarAlerta(AlertType.WARNING, "Validación Fallida", "Datos incorrectos", "Corrige los errores antes de actualizar.");
            return;
        }

        seleccionado.setUsuario(txtUsuario.getText().trim());
        seleccionado.setNombres(txtNombres.getText().trim());
        seleccionado.setApellidos(txtApellidos.getText().trim());
        seleccionado.setCorreo(txtCorreo.getText().trim());
        seleccionado.setContrasena(txtContrasena.getText().trim());
        seleccionado.setRol(cmbRol.getValue());
        seleccionado.setEstado(cmbEstado.getValue());
        seleccionado.setRutaImagen(rutaImagenSeleccionada);
        seleccionado.setEditado(LocalDateTime.now().format(FORMATO_FECHA_HORA));

        tblAdministradores.refresh();
        mostrarAlerta(AlertType.INFORMATION, "Actualización Exitosa", "Registro Actualizado", "Los datos del administrador han sido modificados.");
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Administrador seleccionado = tblAdministradores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin Selección", "Aviso", "Selecciona un registro de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar Registro");
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar al administrador: " + seleccionado.getNombres() + " " + seleccionado.getApellidos() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            listaAdministradores.remove(seleccionado);
            limpiarFormularioCompleto();
            restablecerEstadoInicialBotones();
            mostrarAlerta(AlertType.INFORMATION, "Registro Eliminado", "Eliminación Exitosa", "El administrador fue eliminado de la tabla.");
        }
    }

    @FXML
    private void handleLimpiar(ActionEvent event) {
        // "El botón Limpiar/Cancelar deberá restablecer los campos, los mensajes y el estado inicial de los botones."
        limpiarFormularioCompleto();
        restablecerEstadoInicialBotones();
    }

    private void limpiarFormularioCompleto() {
        limpiandoFormulario = true;
        txtId.clear();
        txtUsuario.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        txtContrasena.clear();
        cmbRol.setValue(null);
        cmbEstado.setValue(null);
        txtCreado.clear();
        txtEditado.clear();
        handleQuitarImagen(null);
        tblAdministradores.getSelectionModel().clearSelection();
        ocultarTodosLosMensajesDeError();
        limpiandoFormulario = false;
    }

    // =========================================================================
    // SELECCIÓN Y GESTIÓN DE IMAGEN
    // =========================================================================
    @FXML
    private void handleSeleccionarImagen(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Imagen de Perfil");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );
        File archivo = fc.showOpenDialog(txtId.getScene().getWindow());
        if (archivo != null) {
            rutaImagenSeleccionada = archivo.getAbsolutePath();
            lblRutaImagen.setText(archivo.getName());
            cargarPreviewImagen(rutaImagenSeleccionada);
        }
    }

    @FXML
    private void handleQuitarImagen(ActionEvent event) {
        rutaImagenSeleccionada = null;
        lblRutaImagen.setText("Sin imagen seleccionada");
        imgFotoPreview.setImage(null);
    }

    private void cargarPreviewImagen(String ruta) {
        if (ruta != null && !ruta.isEmpty()) {
            File f = new File(ruta);
            if (f.exists()) {
                imgFotoPreview.setImage(new Image(f.toURI().toString()));
                return;
            }
        }
        imgFotoPreview.setImage(null);
    }

    // =========================================================================
    // NAVEGACIÓN Y UTILIDADES
    // =========================================================================
    @FXML
    private void handleVolverCines(ActionEvent event) {
        try {
            App.setRoot("cines");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Navegación", "No se pudo volver a la vista principal", e.getMessage());
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Getters para pruebas unitarias / verificación automatizada
    public ObservableList<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public Button getBtnNuevo() {
        return btnNuevo;
    }

    public Button getBtnValidar() {
        return btnValidar;
    }

    public Button getBtnGuardar() {
        return btnGuardar;
    }

    public Button getBtnEditar() {
        return btnEditar;
    }

    public Button getBtnEliminar() {
        return btnEliminar;
    }

    public Label getLblErrorId() {
        return lblErrorId;
    }

    public Label getLblErrorUsuario() {
        return lblErrorUsuario;
    }

    public Label getLblErrorNombres() {
        return lblErrorNombres;
    }

    public Label getLblErrorApellidos() {
        return lblErrorApellidos;
    }

    public Label getLblErrorCorreo() {
        return lblErrorCorreo;
    }

    public Label getLblErrorContrasena() {
        return lblErrorContrasena;
    }

    public Label getLblErrorRol() {
        return lblErrorRol;
    }

    public Label getLblErrorEstado() {
        return lblErrorEstado;
    }
}
