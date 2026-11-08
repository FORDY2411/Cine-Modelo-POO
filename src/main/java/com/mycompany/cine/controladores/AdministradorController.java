package com.mycompany.cine.controladores;

import com.mycompany.cine.daos.AdministradorDAO;
import com.mycompany.cine.daos.GestorImagenes;
import com.mycompany.cine.modelo.Administrador;
import com.mycompany.cine.modelo.EEstados;
import com.mycompany.cine.modelo.ERoles;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * Controlador JavaFX con comunicación directa al DAO para la gestión de Administradores.
 * Incluye gestión física de archivos de imagen (copia a carpeta local y borrado de disco, Actividad 3.9).
 */
public class AdministradorController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtClaveAcceso;
    @FXML
    private ComboBox<ERoles> cmbRol;
    @FXML
    private ComboBox<EEstados> cmbEstado;
    @FXML
    private TextField txtRutaImagen;
    @FXML
    private ImageView imgVistaPrevia;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Administrador> tblAdministradores;
    @FXML
    private TableColumn<Administrador, Integer> colId;
    @FXML
    private TableColumn<Administrador, String> colNombre;
    @FXML
    private TableColumn<Administrador, String> colEmail;
    @FXML
    private TableColumn<Administrador, String> colUsuario;
    @FXML
    private TableColumn<Administrador, String> colRol;
    @FXML
    private TableColumn<Administrador, String> colEstado;
    @FXML
    private TableColumn<Administrador, String> colRutaImagen;

    // Instanciación directa de la capa DAO
    private final AdministradorDAO administradorDAO = new AdministradorDAO();
    private final ObservableList<Administrador> listaObservable = FXCollections.observableArrayList();

    // Archivo de imagen seleccionado pendiente de ser copiado al guardar o actualizar
    private File archivoImagenSeleccionado = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vinculación de columnas con los atributos del modelo
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRutaImagen.setCellValueFactory(new PropertyValueFactory<>("rutaImagen"));

        // Carga de opciones desde los enumeradores
        cmbRol.setItems(FXCollections.observableArrayList(ERoles.values()));
        cmbRol.setValue(ERoles.ADMINISTRADOR);

        cmbEstado.setItems(FXCollections.observableArrayList(EEstados.values()));
        cmbEstado.setValue(EEstados.ACTIVO);

        tblAdministradores.setItems(listaObservable);

        // Listener para autocompletar el formulario al hacer clic en una fila
        tblAdministradores.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> mostrarDetalleAdministrador(newValue)
        );

        // Consulta inicial de registros desde la base de datos
        actualizarTabla();
    }

    @FXML
    private void btnSeleccionarImagenOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto de Perfil");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File archivoSeleccionado = fileChooser.showOpenDialog(txtId.getScene().getWindow());
        if (archivoSeleccionado != null) {
            this.archivoImagenSeleccionado = archivoSeleccionado;
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
            cargarImagenVistaPrevia(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void btnGuardarOnAction(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos != null ? txtApellidos.getText().trim() : "";
        String telefono = txtTelefono != null ? txtTelefono.getText().trim() : "";
        String email = txtEmail.getText().trim();
        String direccion = txtDireccion != null ? txtDireccion.getText().trim() : "";
        String usuario = txtUsuario.getText().trim();
        String clave = txtClaveAcceso.getText().trim();
        ERoles rol = cmbRol.getValue();
        EEstados estado = cmbEstado.getValue();
        String rutaImagen = txtRutaImagen.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || usuario.isEmpty() || clave.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Requeridos",
                    "Nombre, Email, Usuario y Contraseña son obligatorios.");
            return;
        }

        try {
            // Si los apellidos no fueron ingresados por separado pero el nombre incluye espacios
            if (apellidos.isEmpty() && nombre.contains(" ")) {
                int ultimoEspacio = nombre.lastIndexOf(" ");
                apellidos = nombre.substring(ultimoEspacio + 1);
                nombre = nombre.substring(0, ultimoEspacio);
            } else if (apellidos.isEmpty()) {
                apellidos = nombre;
            }

            // Copiar físicamente la imagen al proyecto si fue seleccionada (Actividad 3.9)
            if (archivoImagenSeleccionado != null) {
                rutaImagen = GestorImagenes.copiarImagenAlProyecto(archivoImagenSeleccionado, "administradores", usuario);
            } else if (rutaImagen.isEmpty()) {
                rutaImagen = GestorImagenes.IMAGEN_POR_DEFECTO_USUARIO;
            }

            Administrador nuevoAdmin = new Administrador(
                    0, usuario, nombre, apellidos, email, clave,
                    rol != null ? rol.getDescripcion() : "Administrador",
                    rutaImagen,
                    estado != null ? estado.getDescripcion() : "Activo",
                    null, null
            );
            nuevoAdmin.setTelefono(telefono);
            nuevoAdmin.setDireccion(direccion);

            // Registro directo mediante el DAO
            if (administradorDAO.guardar(nuevoAdmin)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "Administrador registrado correctamente en la base de datos.");
                archivoImagenSeleccionado = null;
                actualizarTabla();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "No se pudo guardar el registro. Verifique la conexión a MySQL o que el usuario/correo no estén duplicados.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al procesar el guardado: " + e.getMessage());
        }
    }

    @FXML
    private void btnActualizarOnAction(ActionEvent event) {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida",
                    "Seleccione un administrador de la tabla para actualizar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos != null ? txtApellidos.getText().trim() : "";
            String telefono = txtTelefono != null ? txtTelefono.getText().trim() : "";
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion != null ? txtDireccion.getText().trim() : "";
            String usuario = txtUsuario.getText().trim();
            String clave = txtClaveAcceso.getText().trim();
            ERoles rol = cmbRol.getValue();
            EEstados estado = cmbEstado.getValue();
            String rutaImagen = txtRutaImagen.getText().trim();

            if (nombre.isEmpty() || email.isEmpty() || usuario.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Requeridos",
                        "Nombre, Email y Usuario no pueden estar vacíos al actualizar.");
                return;
            }

            if (apellidos.isEmpty() && nombre.contains(" ")) {
                int ultimoEspacio = nombre.lastIndexOf(" ");
                apellidos = nombre.substring(ultimoEspacio + 1);
                nombre = nombre.substring(0, ultimoEspacio);
            }

            Administrador adminActual = administradorDAO.obtenerPorId(id);

            // Si se seleccionó una imagen nueva, copiarla y borrar la anterior del disco
            if (archivoImagenSeleccionado != null) {
                if (adminActual != null && adminActual.getRutaImagen() != null) {
                    GestorImagenes.eliminarImagenDelProyecto(adminActual.getRutaImagen());
                }
                rutaImagen = GestorImagenes.copiarImagenAlProyecto(archivoImagenSeleccionado, "administradores", usuario);
            } else if (rutaImagen.isEmpty() && adminActual != null) {
                rutaImagen = adminActual.getRutaImagen();
            }

            Administrador adminActualizar = new Administrador(
                    id, usuario, nombre, apellidos, email, clave,
                    rol != null ? rol.getDescripcion() : "Administrador",
                    rutaImagen.isEmpty() ? GestorImagenes.IMAGEN_POR_DEFECTO_USUARIO : rutaImagen,
                    estado != null ? estado.getDescripcion() : "Activo",
                    null, null
            );
            adminActualizar.setTelefono(telefono);
            adminActualizar.setDireccion(direccion);

            // Actualización directa mediante el DAO
            if (administradorDAO.actualizar(adminActualizar)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "Registro actualizado correctamente en la base de datos.");
                archivoImagenSeleccionado = null;
                actualizarTabla();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "No se pudo actualizar la información en la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Identificador no válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void btnEliminarOnAction(ActionEvent event) {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida",
                    "Seleccione un registro de la tabla para eliminar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            Administrador adminEliminar = administradorDAO.obtenerPorId(id);

            // Eliminación directa mediante el DAO
            if (administradorDAO.eliminar(id)) {
                // Borrar el archivo de imagen del proyecto (Actividad 3.9)
                if (adminEliminar != null && adminEliminar.getRutaImagen() != null) {
                    GestorImagenes.eliminarImagenDelProyecto(adminEliminar.getRutaImagen());
                }

                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "Administrador eliminado correctamente de la base de datos.");
                actualizarTabla();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "No se pudo eliminar el registro de la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Identificador no válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato Inválido", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void btnLimpiarOnAction(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    private void handleVolverTaquilla(ActionEvent event) {
        try {
            com.mycompany.cine.App.setRoot("cines");
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo regresar a la Taquilla: " + ex.getMessage());
        }
    }

    @FXML
    private void handleSalirLogin(ActionEvent event) {
        try {
            com.mycompany.cine.App.setRoot("login");
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo regresar al Login: " + ex.getMessage());
        }
    }

    private void mostrarDetalleAdministrador(Administrador admin) {
        if (admin != null) {
            txtId.setText(String.valueOf(admin.getId()));
            txtNombre.setText(admin.getNombre() != null ? admin.getNombre() : admin.getNombres());
            if (txtApellidos != null) {
                txtApellidos.setText(admin.getApellido() != null ? admin.getApellido() : admin.getApellidos());
            }
            if (txtTelefono != null) {
                txtTelefono.setText(admin.getTelefono() != null ? admin.getTelefono() : "");
            }
            txtEmail.setText(admin.getEmail() != null ? admin.getEmail() : admin.getCorreo());
            if (txtDireccion != null) {
                txtDireccion.setText(admin.getDireccion() != null ? admin.getDireccion() : "");
            }
            txtUsuario.setText(admin.getUsuario() != null ? admin.getUsuario() : "");
            txtClaveAcceso.setText(admin.getContrasena() != null ? admin.getContrasena() : "");
            cmbRol.setValue(admin.getRolEnum());
            cmbEstado.setValue(admin.getEstadoEnum());
            txtRutaImagen.setText(admin.getRutaImagen() != null ? admin.getRutaImagen() : "");
            cargarImagenVistaPrevia(admin.getRutaImagen());
            archivoImagenSeleccionado = null;
        }
    }

    private void cargarImagenVistaPrevia(String ruta) {
        Image imagen = GestorImagenes.cargarImagen(ruta);
        imgVistaPrevia.setImage(imagen);
    }

    private void actualizarTabla() {
        // Consulta directa de datos desde el DAO
        listaObservable.setAll(administradorDAO.listarTodos());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        if (txtApellidos != null) txtApellidos.setText("");
        if (txtTelefono != null) txtTelefono.setText("");
        txtEmail.setText("");
        if (txtDireccion != null) txtDireccion.setText("");
        txtUsuario.setText("");
        txtClaveAcceso.setText("");
        cmbRol.setValue(ERoles.ADMINISTRADOR);
        cmbEstado.setValue(EEstados.ACTIVO);
        txtRutaImagen.setText("");
        imgVistaPrevia.setImage(null);
        archivoImagenSeleccionado = null;
        tblAdministradores.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
