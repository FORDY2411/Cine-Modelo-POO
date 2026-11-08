package com.mycompany.cine.controladores;

import com.mycompany.cine.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador para la vista inicial de Login (Actividad 3.6).
 * Gestiona el acceso directo a la ventana principal sin requerir credenciales
 * y la finalización total de la aplicación.
 * 
 * @author Fordiis
 */
public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnEntrar;
    @FXML private Button btnCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de la vista si fuera requerida
    }

    /**
     * Abre la ventana principal (cines.fxml) sin solicitar usuario ni contraseña,
     * dando cumplimiento estricto a las especificaciones de la Actividad 3.6.
     */
    @FXML
    private void handleEntrar(ActionEvent event) {
        try {
            App.setRoot("cines");
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de Navegación");
            alert.setHeaderText("No se pudo abrir la ventana principal");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Finaliza completamente la ejecución del programa.
     */
    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
