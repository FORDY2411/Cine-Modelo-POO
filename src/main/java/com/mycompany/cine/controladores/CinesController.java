package com.mycompany.cine.controladores;

import com.mycompany.cine.modelo.Administrador;
import com.mycompany.cine.modelo.Funcion;
import com.mycompany.cine.modelo.Pelicula;
import com.mycompany.cine.modelo.Sala;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Controlador principal para la gestión de vistas de Cine (Actividad 2.8).
 * Administra el CRUD básico de Películas, Salas, Funciones y Personal.
 * 
 * @author Fordiis
 */
public class CinesController implements Initializable {

    // ==========================================
    // CONTROLES DE NAVEGACIÓN Y PANELES
    // ==========================================
    @FXML private Button btnNavPeliculas;
    @FXML private Button btnNavSalas;
    @FXML private Button btnNavFunciones;
    @FXML private Button btnNavUsuarios;
    @FXML private Button btnNavAdministradores;
    @FXML private Button btnNavAdminDAO;
    @FXML private Button btnNavSalirLogin;

    @FXML private VBox pnlPeliculas;
    @FXML private VBox pnlSalas;
    @FXML private VBox pnlFunciones;
    @FXML private VBox pnlUsuarios;

    // ==========================================
    // MÓDULO: PELÍCULAS
    // ==========================================
    @FXML private TextField txtPeliculaId;
    @FXML private TextField txtPeliculaTitulo;
    @FXML private TextField txtPeliculaDuracion;
    @FXML private ComboBox<String> cmbPeliculaClasificacion;
    @FXML private ComboBox<String> cmbPeliculaGenero;
    @FXML private ComboBox<String> cmbPeliculaEstado;
    @FXML private ImageView imgPeliculaPreview;
    @FXML private Label lblPeliculaRuta;

    @FXML private TableView<Pelicula> tblPeliculas;
    @FXML private TableColumn<Pelicula, Integer> colPeliculaId;
    @FXML private TableColumn<Pelicula, String> colPeliculaTitulo;
    @FXML private TableColumn<Pelicula, Integer> colPeliculaDuracion;
    @FXML private TableColumn<Pelicula, String> colPeliculaClasificacion;
    @FXML private TableColumn<Pelicula, String> colPeliculaGenero;
    @FXML private TableColumn<Pelicula, String> colPeliculaEstado;

    private final ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();
    private String rutaImagenSeleccionada = null;

    // ==========================================
    // MÓDULO: SALAS
    // ==========================================
    @FXML private TextField txtSalaId;
    @FXML private TextField txtSalaNumero;
    @FXML private TextField txtSalaCapacidad;
    @FXML private ComboBox<String> cmbSalaTecnologia;
    @FXML private ComboBox<String> cmbSalaEstado;

    @FXML private TableView<Sala> tblSalas;
    @FXML private TableColumn<Sala, Integer> colSalaId;
    @FXML private TableColumn<Sala, Integer> colSalaNumero;
    @FXML private TableColumn<Sala, Integer> colSalaCapacidad;
    @FXML private TableColumn<Sala, String> colSalaTecnologia;
    @FXML private TableColumn<Sala, String> colSalaEstado;

    private final ObservableList<Sala> listaSalas = FXCollections.observableArrayList();

    // ==========================================
    // MÓDULO: FUNCIONES
    // ==========================================
    @FXML private TextField txtFuncionId;
    @FXML private ComboBox<Pelicula> cmbFuncionPelicula;
    @FXML private ComboBox<Sala> cmbFuncionSala;
    @FXML private DatePicker dpFuncionFecha;
    @FXML private ComboBox<String> cmbFuncionHora;
    @FXML private TextField txtFuncionPrecio;
    @FXML private ComboBox<String> cmbFuncionEstado;

    @FXML private TableView<Funcion> tblFunciones;
    @FXML private TableColumn<Funcion, Integer> colFuncionId;
    @FXML private TableColumn<Funcion, String> colFuncionPelicula;
    @FXML private TableColumn<Funcion, String> colFuncionSala;
    @FXML private TableColumn<Funcion, String> colFuncionFechaHora;
    @FXML private TableColumn<Funcion, String> colFuncionPrecio;
    @FXML private TableColumn<Funcion, String> colFuncionEstado;

    private final ObservableList<Funcion> listaFunciones = FXCollections.observableArrayList();

    // ==========================================
    // MÓDULO: USUARIOS / PERSONAL
    // ==========================================
    @FXML private TextField txtUsuarioId;
    @FXML private TextField txtUsuarioNombre;
    @FXML private TextField txtUsuarioApellido;
    @FXML private TextField txtUsuarioEmail;
    @FXML private PasswordField txtUsuarioContrasena;
    @FXML private ComboBox<String> cmbUsuarioNivel;
    @FXML private ComboBox<String> cmbUsuarioTurno;

    @FXML private TableView<Administrador> tblUsuarios;
    @FXML private TableColumn<Administrador, Integer> colUsuarioId;
    @FXML private TableColumn<Administrador, String> colUsuarioNombre;
    @FXML private TableColumn<Administrador, String> colUsuarioApellido;
    @FXML private TableColumn<Administrador, String> colUsuarioEmail;
    @FXML private TableColumn<Administrador, String> colUsuarioNivel;
    @FXML private TableColumn<Administrador, String> colUsuarioTurno;

    private final ObservableList<Administrador> listaUsuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarModuloPeliculas();
        inicializarModuloSalas();
        inicializarModuloFunciones();
        inicializarModuloUsuarios();

        // Mostrar películas por defecto al abrir
        handleMostrarPeliculas(null);
    }

    // =========================================================================
    // NAVEGACIÓN ENTRE MÓDULOS
    // =========================================================================
    @FXML
    private void handleMostrarPeliculas(ActionEvent event) {
        activarPanel(pnlPeliculas, btnNavPeliculas);
    }

    @FXML
    private void handleMostrarSalas(ActionEvent event) {
        activarPanel(pnlSalas, btnNavSalas);
    }

    @FXML
    private void handleMostrarFunciones(ActionEvent event) {
        // Actualizamos los combos de películas y salas con los registros actuales
        cmbFuncionPelicula.setItems(listaPeliculas);
        cmbFuncionSala.setItems(listaSalas);
        activarPanel(pnlFunciones, btnNavFunciones);
    }

    @FXML
    private void handleMostrarUsuarios(ActionEvent event) {
        activarPanel(pnlUsuarios, btnNavUsuarios);
    }

    @FXML
    private void handleAbrirAdministradores(ActionEvent event) {
        try {
            com.mycompany.cine.App.setRoot("administradores");
        } catch (Exception ex) {
            mostrarAlerta("Error de Navegación", "No se pudo cargar la vista de administradores", ex.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleAbrirAdminDAO(ActionEvent event) {
        try {
            com.mycompany.cine.App.setRoot("administrador");
        } catch (Exception ex) {
            mostrarAlerta("Error de Navegación", "No se pudo cargar la vista DAO de administradores", ex.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleSalirLogin(ActionEvent event) {
        try {
            com.mycompany.cine.App.setRoot("login");
        } catch (Exception ex) {
            mostrarAlerta("Error de Navegación", "No se pudo regresar al Login", ex.getMessage(), AlertType.ERROR);
        }
    }

    private void activarPanel(VBox panelActivo, Button botonActivo) {
        // Ocultar todos
        pnlPeliculas.setVisible(false);
        pnlSalas.setVisible(false);
        pnlFunciones.setVisible(false);
        pnlUsuarios.setVisible(false);

        // Resetear color de botones laterales
        String estiloNormal = "-fx-background-color: #334155; -fx-text-fill: white; -fx-cursor: hand;";
        String estiloActivo = "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";

        btnNavPeliculas.setStyle(estiloNormal);
        btnNavSalas.setStyle(estiloNormal);
        btnNavFunciones.setStyle(estiloNormal);
        btnNavUsuarios.setStyle(estiloNormal);

        // Mostrar el seleccionado
        panelActivo.setVisible(true);
        botonActivo.setStyle(estiloActivo);
    }

    // =========================================================================
    // LÓGICA MÓDULO PELÍCULAS
    // =========================================================================
    private void inicializarModuloPeliculas() {
        // Llenar ComboBoxes
        cmbPeliculaClasificacion.getItems().addAll("AA", "A", "B", "B15", "C", "D");
        cmbPeliculaGenero.getItems().addAll("Acción", "Aventura", "Comedia", "Drama", "Terror", "Ciencia Ficción", "Animación");
        cmbPeliculaEstado.getItems().addAll("En Cartelera", "Próximamente", "Retirada");

        // Configurar columnas de la tabla
        colPeliculaId.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        colPeliculaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPeliculaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colPeliculaClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colPeliculaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPeliculaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoPelicula"));

        tblPeliculas.setItems(listaPeliculas);

        // Al dar clic en una fila, cargar datos al formulario
        tblPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, peli) -> {
            if (peli != null) {
                txtPeliculaId.setText(String.valueOf(peli.getIdPelicula()));
                txtPeliculaTitulo.setText(peli.getTitulo());
                txtPeliculaDuracion.setText(String.valueOf(peli.getDuracion()));
                cmbPeliculaClasificacion.setValue(peli.getClasificacion());
                cmbPeliculaGenero.setValue(peli.getGenero());
                cmbPeliculaEstado.setValue(peli.getEstadoPelicula());

                // Cargar imagen si tiene ruta
                if (peli.getRutaImagen() != null && !peli.getRutaImagen().isEmpty()) {
                    rutaImagenSeleccionada = peli.getRutaImagen();
                    lblPeliculaRuta.setText(new File(peli.getRutaImagen()).getName());
                    try {
                        imgPeliculaPreview.setImage(new Image(new File(peli.getRutaImagen()).toURI().toString()));
                    } catch (Exception ex) {
                        imgPeliculaPreview.setImage(null);
                        lblPeliculaRuta.setText("No se pudo cargar la imagen");
                    }
                } else {
                    handleQuitarImagen(null);
                }
            }
        });
    }

    @FXML
    private void handleSeleccionarImagen(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Póster de la Película");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg"));
        File archivo = fc.showOpenDialog(txtPeliculaTitulo.getScene().getWindow());

        if (archivo != null) {
            rutaImagenSeleccionada = archivo.getAbsolutePath();
            lblPeliculaRuta.setText(archivo.getName());
            imgPeliculaPreview.setImage(new Image(archivo.toURI().toString()));
        }
    }

    @FXML
    private void handleQuitarImagen(ActionEvent event) {
        rutaImagenSeleccionada = null;
        lblPeliculaRuta.setText("Sin imagen seleccionada");
        imgPeliculaPreview.setImage(null);
    }

    @FXML
    private void handleNuevoPelicula(ActionEvent event) {
        handleLimpiarPelicula(event);
    }

    @FXML
    private void handleLimpiarPelicula(ActionEvent event) {
        txtPeliculaId.clear();
        txtPeliculaTitulo.clear();
        txtPeliculaDuracion.clear();
        cmbPeliculaClasificacion.setValue(null);
        cmbPeliculaGenero.setValue(null);
        cmbPeliculaEstado.setValue(null);
        handleQuitarImagen(null);
        tblPeliculas.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGuardarPelicula(ActionEvent event) {
        try {
            // Validaciones básicas de campos vacíos
            if (txtPeliculaId.getText().trim().isEmpty() || txtPeliculaTitulo.getText().trim().isEmpty()
                    || txtPeliculaDuracion.getText().trim().isEmpty() || cmbPeliculaClasificacion.getValue() == null
                    || cmbPeliculaGenero.getValue() == null || cmbPeliculaEstado.getValue() == null) {
                mostrarAlerta("Error de Validación", "Campos incompletos", "Por favor completa todos los campos del formulario.", AlertType.WARNING);
                return;
            }

            int id = Integer.parseInt(txtPeliculaId.getText().trim());
            int duracion = Integer.parseInt(txtPeliculaDuracion.getText().trim());

            // Validar ID duplicado
            for (Pelicula p : listaPeliculas) {
                if (p.getIdPelicula() == id) {
                    mostrarAlerta("ID Duplicado", "Ya existe una película con este ID", "Elige un ID diferente para la nueva película.", AlertType.ERROR);
                    return;
                }
            }

            Pelicula nueva = new Pelicula(id, txtPeliculaTitulo.getText().trim(), duracion,
                    cmbPeliculaClasificacion.getValue(), cmbPeliculaGenero.getValue(),
                    cmbPeliculaEstado.getValue(), rutaImagenSeleccionada);

            listaPeliculas.add(nueva);
            mostrarAlerta("Éxito", "Película Registrada", "La película se ha guardado correctamente.", AlertType.INFORMATION);
            handleLimpiarPelicula(null);

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Incorrecto", "Valores numéricos no válidos", "El ID y la duración deben ser números enteros válidos.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Datos no permitidos", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditarPelicula(ActionEvent event) {
        Pelicula seleccionada = tblPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una película de la tabla para editarla.", AlertType.WARNING);
            return;
        }

        try {
            int duracion = Integer.parseInt(txtPeliculaDuracion.getText().trim());

            seleccionada.setTitulo(txtPeliculaTitulo.getText().trim());
            seleccionada.setDuracion(duracion);
            seleccionada.setClasificacion(cmbPeliculaClasificacion.getValue());
            seleccionada.setGenero(cmbPeliculaGenero.getValue());
            seleccionada.setEstadoPelicula(cmbPeliculaEstado.getValue());
            seleccionada.setRutaImagen(rutaImagenSeleccionada);

            tblPeliculas.refresh();
            mostrarAlerta("Éxito", "Película Actualizada", "Los datos de la película se han modificado correctamente.", AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Incorrecto", "Duración inválida", "La duración debe ser un número entero mayor a 0.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Datos no permitidos", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminarPelicula(ActionEvent event) {
        Pelicula seleccionada = tblPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una película de la tabla para eliminarla.", AlertType.WARNING);
            return;
        }

        if (confirmarEliminacion("¿Estás seguro de que deseas eliminar la película '" + seleccionada.getTitulo() + "'?")) {
            listaPeliculas.remove(seleccionada);
            handleLimpiarPelicula(null);
            mostrarAlerta("Éxito", "Película Eliminada", "El registro ha sido eliminado satisfactoriamente.", AlertType.INFORMATION);
        }
    }

    // =========================================================================
    // LÓGICA MÓDULO SALAS
    // =========================================================================
    private void inicializarModuloSalas() {
        cmbSalaTecnologia.getItems().addAll("2D Digital", "3D MacroXE", "IMAX Laser", "4DX Extreme", "VIP Tradicional");
        cmbSalaEstado.getItems().addAll("Disponible", "Mantenimiento", "Limpieza");

        colSalaId.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        colSalaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colSalaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colSalaTecnologia.setCellValueFactory(new PropertyValueFactory<>("tecnologia"));
        colSalaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoSala"));

        tblSalas.setItems(listaSalas);

        tblSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sala) -> {
            if (sala != null) {
                txtSalaId.setText(String.valueOf(sala.getIdSala()));
                txtSalaNumero.setText(String.valueOf(sala.getNumero()));
                txtSalaCapacidad.setText(String.valueOf(sala.getCapacidad()));
                cmbSalaTecnologia.setValue(sala.getTecnologia());
                cmbSalaEstado.setValue(sala.getEstadoSala());
            }
        });
    }

    @FXML
    private void handleNuevoSala(ActionEvent event) {
        handleLimpiarSala(event);
    }

    @FXML
    private void handleLimpiarSala(ActionEvent event) {
        txtSalaId.clear();
        txtSalaNumero.clear();
        txtSalaCapacidad.clear();
        cmbSalaTecnologia.setValue(null);
        cmbSalaEstado.setValue(null);
        tblSalas.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGuardarSala(ActionEvent event) {
        try {
            if (txtSalaId.getText().trim().isEmpty() || txtSalaNumero.getText().trim().isEmpty()
                    || txtSalaCapacidad.getText().trim().isEmpty() || cmbSalaTecnologia.getValue() == null
                    || cmbSalaEstado.getValue() == null) {
                mostrarAlerta("Error", "Campos incompletos", "Por favor llena todos los campos de la sala.", AlertType.WARNING);
                return;
            }

            int id = Integer.parseInt(txtSalaId.getText().trim());
            int num = Integer.parseInt(txtSalaNumero.getText().trim());
            int cap = Integer.parseInt(txtSalaCapacidad.getText().trim());

            for (Sala s : listaSalas) {
                if (s.getIdSala() == id) {
                    mostrarAlerta("ID Duplicado", "Ya existe una sala con este ID", "Elige otro identificador.", AlertType.ERROR);
                    return;
                }
            }

            Sala nueva = new Sala(id, num, cap, cmbSalaTecnologia.getValue(), cmbSalaEstado.getValue());
            listaSalas.add(nueva);
            mostrarAlerta("Éxito", "Sala Guardada", "La sala se registró correctamente.", AlertType.INFORMATION);
            handleLimpiarSala(null);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Valores numéricos inválidos", "El ID, número y capacidad deben ser números enteros.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Datos no válidos", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditarSala(ActionEvent event) {
        Sala seleccionada = tblSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una sala de la tabla para editarla.", AlertType.WARNING);
            return;
        }

        try {
            seleccionada.setNumero(Integer.parseInt(txtSalaNumero.getText().trim()));
            seleccionada.setCapacidad(Integer.parseInt(txtSalaCapacidad.getText().trim()));
            seleccionada.setTecnologia(cmbSalaTecnologia.getValue());
            seleccionada.setEstadoSala(cmbSalaEstado.getValue());

            tblSalas.refresh();
            mostrarAlerta("Éxito", "Sala Modificada", "Los datos de la sala fueron actualizados.", AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Valores numéricos inválidos", "Verifica el número y la capacidad.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Datos no válidos", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminarSala(ActionEvent event) {
        Sala seleccionada = tblSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una sala de la tabla para eliminarla.", AlertType.WARNING);
            return;
        }

        if (confirmarEliminacion("¿Deseas eliminar la Sala " + seleccionada.getNumero() + "?")) {
            listaSalas.remove(seleccionada);
            handleLimpiarSala(null);
            mostrarAlerta("Éxito", "Sala Eliminada", "La sala ha sido eliminada.", AlertType.INFORMATION);
        }
    }

    // =========================================================================
    // LÓGICA MÓDULO FUNCIONES
    // =========================================================================
    private void inicializarModuloFunciones() {
        cmbFuncionHora.getItems().addAll("11:30", "13:00", "15:15", "17:40", "19:50", "21:30", "22:45");
        cmbFuncionEstado.getItems().addAll("Programada", "En Proyección", "Finalizada", "Agotada");

        colFuncionId.setCellValueFactory(new PropertyValueFactory<>("idFuncion"));
        colFuncionPelicula.setCellValueFactory(new PropertyValueFactory<>("tituloPelicula"));
        colFuncionSala.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        colFuncionFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraFormateada"));
        colFuncionPrecio.setCellValueFactory(new PropertyValueFactory<>("precioFormateado"));
        colFuncionEstado.setCellValueFactory(new PropertyValueFactory<>("estadoFuncion"));

        tblFunciones.setItems(listaFunciones);

        tblFunciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, f) -> {
            if (f != null) {
                txtFuncionId.setText(String.valueOf(f.getIdFuncion()));
                cmbFuncionPelicula.setValue(f.getPelicula());
                cmbFuncionSala.setValue(f.getSala());
                if (f.getFechaHora() != null) {
                    dpFuncionFecha.setValue(f.getFechaHora().toLocalDate());
                    cmbFuncionHora.setValue(String.format("%02d:%02d", f.getFechaHora().getHour(), f.getFechaHora().getMinute()));
                }
                txtFuncionPrecio.setText(f.getPrecioBase() != null ? f.getPrecioBase().toPlainString() : "");
                cmbFuncionEstado.setValue(f.getEstadoFuncion());
            }
        });
    }

    @FXML
    private void handleNuevoFuncion(ActionEvent event) {
        handleLimpiarFuncion(event);
    }

    @FXML
    private void handleLimpiarFuncion(ActionEvent event) {
        txtFuncionId.clear();
        cmbFuncionPelicula.setValue(null);
        cmbFuncionSala.setValue(null);
        dpFuncionFecha.setValue(null);
        cmbFuncionHora.setValue(null);
        txtFuncionPrecio.clear();
        cmbFuncionEstado.setValue(null);
        tblFunciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGuardarFuncion(ActionEvent event) {
        try {
            if (txtFuncionId.getText().trim().isEmpty() || cmbFuncionPelicula.getValue() == null
                    || cmbFuncionSala.getValue() == null || dpFuncionFecha.getValue() == null
                    || cmbFuncionHora.getValue() == null || txtFuncionPrecio.getText().trim().isEmpty()
                    || cmbFuncionEstado.getValue() == null) {
                mostrarAlerta("Campos Requeridos", "Faltan datos", "Completa todos los datos para programar la función.", AlertType.WARNING);
                return;
            }

            int id = Integer.parseInt(txtFuncionId.getText().trim());

            for (Funcion fn : listaFunciones) {
                if (fn.getIdFuncion() == id) {
                    mostrarAlerta("ID Duplicado", "Ya existe una función con este ID", "Elige otro identificador.", AlertType.ERROR);
                    return;
                }
            }

            LocalDate fecha = dpFuncionFecha.getValue();
            String[] partesHora = cmbFuncionHora.getValue().split(":");
            LocalTime hora = LocalTime.of(Integer.parseInt(partesHora[0]), Integer.parseInt(partesHora[1]));
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            BigDecimal precio = new BigDecimal(txtFuncionPrecio.getText().trim());

            Funcion nueva = new Funcion(id, cmbFuncionPelicula.getValue(), cmbFuncionSala.getValue(),
                    fechaHora, precio, cmbFuncionEstado.getValue());

            listaFunciones.add(nueva);
            mostrarAlerta("Éxito", "Función Creada", "La función se ha programado exitosamente.", AlertType.INFORMATION);
            handleLimpiarFuncion(null);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error Numérico", "Precio o ID inválidos", "Asegúrate de ingresar números válidos para el ID y el Precio (ej. 85.00).", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Datos no permitidos", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditarFuncion(ActionEvent event) {
        Funcion seleccionada = tblFunciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una función de la tabla para editarla.", AlertType.WARNING);
            return;
        }

        try {
            LocalDate fecha = dpFuncionFecha.getValue();
            String[] partesHora = cmbFuncionHora.getValue().split(":");
            LocalTime hora = LocalTime.of(Integer.parseInt(partesHora[0]), Integer.parseInt(partesHora[1]));
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            BigDecimal precio = new BigDecimal(txtFuncionPrecio.getText().trim());

            seleccionada.setPelicula(cmbFuncionPelicula.getValue());
            seleccionada.setSala(cmbFuncionSala.getValue());
            seleccionada.setFechaHora(fechaHora);
            seleccionada.setPrecioBase(precio);
            seleccionada.setEstadoFuncion(cmbFuncionEstado.getValue());

            tblFunciones.refresh();
            mostrarAlerta("Éxito", "Función Actualizada", "Los datos de la función se modificaron correctamente.", AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminarFuncion(ActionEvent event) {
        Funcion seleccionada = tblFunciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona una función para eliminarla.", AlertType.WARNING);
            return;
        }

        if (confirmarEliminacion("¿Deseas eliminar la función con ID #" + seleccionada.getIdFuncion() + "?")) {
            listaFunciones.remove(seleccionada);
            handleLimpiarFuncion(null);
            mostrarAlerta("Éxito", "Función Eliminada", "La función ha sido cancelada y retirada.", AlertType.INFORMATION);
        }
    }

    // =========================================================================
    // LÓGICA MÓDULO USUARIOS / PERSONAL
    // =========================================================================
    private void inicializarModuloUsuarios() {
        cmbUsuarioNivel.getItems().addAll("Administrador General", "Gerente de Turno", "Supervisor de Área", "Taquillero");
        cmbUsuarioTurno.getItems().addAll("En turno", "De licencia", "Ausente");

        colUsuarioId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuarioNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuarioApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colUsuarioEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsuarioNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAcceso"));
        colUsuarioTurno.setCellValueFactory(new PropertyValueFactory<>("estadoTurno"));

        tblUsuarios.setItems(listaUsuarios);

        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, u) -> {
            if (u != null) {
                txtUsuarioId.setText(String.valueOf(u.getId()));
                txtUsuarioNombre.setText(u.getNombre());
                txtUsuarioApellido.setText(u.getApellido());
                txtUsuarioEmail.setText(u.getEmail());
                txtUsuarioContrasena.setText(u.getContrasena() != null ? u.getContrasena() : "");
                cmbUsuarioNivel.setValue(u.getNivelAcceso());
                cmbUsuarioTurno.setValue(u.getEstadoTurno());
            }
        });
    }

    @FXML
    private void handleNuevoUsuario(ActionEvent event) {
        handleLimpiarUsuario(event);
    }

    @FXML
    private void handleLimpiarUsuario(ActionEvent event) {
        txtUsuarioId.clear();
        txtUsuarioNombre.clear();
        txtUsuarioApellido.clear();
        txtUsuarioEmail.clear();
        txtUsuarioContrasena.clear();
        cmbUsuarioNivel.setValue(null);
        cmbUsuarioTurno.setValue(null);
        tblUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGuardarUsuario(ActionEvent event) {
        try {
            if (txtUsuarioId.getText().trim().isEmpty() || txtUsuarioNombre.getText().trim().isEmpty()
                    || txtUsuarioApellido.getText().trim().isEmpty() || txtUsuarioEmail.getText().trim().isEmpty()
                    || cmbUsuarioNivel.getValue() == null || cmbUsuarioTurno.getValue() == null) {
                mostrarAlerta("Campos Incompletos", "Datos requeridos", "Por favor ingresa todos los datos del usuario.", AlertType.WARNING);
                return;
            }

            int id = Integer.parseInt(txtUsuarioId.getText().trim());

            for (Administrador u : listaUsuarios) {
                if (u.getId() == id) {
                    mostrarAlerta("ID Duplicado", "Ya existe un usuario con este ID", "Elige otro identificador.", AlertType.ERROR);
                    return;
                }
            }

            Administrador nuevo = new Administrador(cmbUsuarioNivel.getValue(), id,
                    txtUsuarioNombre.getText().trim(), txtUsuarioApellido.getText().trim(),
                    txtUsuarioEmail.getText().trim());

            nuevo.setContrasena(txtUsuarioContrasena.getText().trim());
            nuevo.setEstadoTurno(cmbUsuarioTurno.getValue());

            listaUsuarios.add(nuevo);
            mostrarAlerta("Éxito", "Usuario Registrado", "El usuario ha sido registrado en el sistema.", AlertType.INFORMATION);
            handleLimpiarUsuario(null);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error Numérico", "ID Inválido", "El ID debe ser un número entero mayor a 0.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Validación no superada", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditarUsuario(ActionEvent event) {
        Administrador seleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona un usuario de la tabla para editarlo.", AlertType.WARNING);
            return;
        }

        try {
            seleccionado.setNombre(txtUsuarioNombre.getText().trim());
            seleccionado.setApellido(txtUsuarioApellido.getText().trim());
            seleccionado.setEmail(txtUsuarioEmail.getText().trim());
            seleccionado.setContrasena(txtUsuarioContrasena.getText().trim());
            seleccionado.setNivelAcceso(cmbUsuarioNivel.getValue());
            seleccionado.setEstadoTurno(cmbUsuarioTurno.getValue());

            tblUsuarios.refresh();
            mostrarAlerta("Éxito", "Usuario Actualizado", "Los datos del usuario han sido modificados.", AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en Modelo", "Validación no superada", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminarUsuario(ActionEvent event) {
        Administrador seleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Sin selección", "Selecciona un usuario para eliminarlo.", AlertType.WARNING);
            return;
        }

        if (confirmarEliminacion("¿Deseas dar de baja a " + seleccionado.getNombre() + " " + seleccionado.getApellido() + "?")) {
            listaUsuarios.remove(seleccionado);
            handleLimpiarUsuario(null);
            mostrarAlerta("Éxito", "Usuario Eliminado", "El registro del usuario fue eliminado.", AlertType.INFORMATION);
        }
    }

    // =========================================================================
    // MÉTODOS AUXILIARES PARA DIÁLOGOS Y ALERTAS
    // =========================================================================
    private void mostrarAlerta(String titulo, String encabezado, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private boolean confirmarEliminacion(String mensaje) {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Esta acción eliminará el registro");
        confirmacion.setContentText(mensaje);

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }
}
