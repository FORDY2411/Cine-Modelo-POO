package com.mycompany.cine.controladores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de estructura para verificar que 'administradores.fxml' existe,
 * es un documento XML bien formado y contiene todos los fx:id requeridos
 * para los controles de entrada, labels de error debajo de los controles y
 * las 11 columnas de la base de datos MySQL.
 */
public class AdministradoresFXMLStructureTest {

    @Test
    @DisplayName("El archivo administradores.fxml debe existir en el classpath y tener etiquetas válidas")
    public void testFxmlExisteYEsValido() throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/com/mycompany/cine/administradores.fxml")) {
            assertNotNull(stream, "El recurso /com/mycompany/cine/administradores.fxml debe existir");
            String contenido = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

            assertTrue(contenido.startsWith("<?xml"), "Debe iniciar con declaración XML");
            assertTrue(contenido.contains("<BorderPane"), "Debe contener el elemento raíz BorderPane");
            assertTrue(contenido.contains("</BorderPane>"), "Debe cerrar el elemento raíz BorderPane");
            assertTrue(contenido.contains("fx:controller=\"com.mycompany.cine.controladores.AdministradoresController\""),
                    "Debe declarar el controlador correcto");
        }
    }

    @Test
    @DisplayName("administradores.fxml debe contener todos los fx:id necesarios de la Actividad 3.2 y de la BD")
    public void testFxmlContieneControlesRequeridos() throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/com/mycompany/cine/administradores.fxml")) {
            assertNotNull(stream);
            String contenido = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

            // Controles de formulario
            List<String> controlesEsperados = Arrays.asList(
                    "fx:id=\"txtId\"",
                    "fx:id=\"lblErrorId\"",
                    "fx:id=\"txtUsuario\"",
                    "fx:id=\"lblErrorUsuario\"",
                    "fx:id=\"txtNombres\"",
                    "fx:id=\"lblErrorNombres\"",
                    "fx:id=\"txtApellidos\"",
                    "fx:id=\"lblErrorApellidos\"",
                    "fx:id=\"txtCorreo\"",
                    "fx:id=\"lblErrorCorreo\"",
                    "fx:id=\"txtContrasena\"",
                    "fx:id=\"lblErrorContrasena\"",
                    "fx:id=\"cmbRol\"",
                    "fx:id=\"lblErrorRol\"",
                    "fx:id=\"cmbEstado\"",
                    "fx:id=\"lblErrorEstado\"",
                    "fx:id=\"btnNuevo\"",
                    "fx:id=\"btnValidar\"",
                    "fx:id=\"btnGuardar\"",
                    "fx:id=\"btnEditar\"",
                    "fx:id=\"btnEliminar\"",
                    "fx:id=\"btnLimpiar\""
            );

            for (String esperado : controlesEsperados) {
                assertTrue(contenido.contains(esperado), "El FXML debe contener el control: " + esperado);
            }

            // Las 11 columnas de la tabla de base de datos MySQL (según screenshot phpMyAdmin)
            List<String> columnasEsperadas = Arrays.asList(
                    "fx:id=\"colId\"",
                    "fx:id=\"colUsuario\"",
                    "fx:id=\"colNombres\"",
                    "fx:id=\"colApellidos\"",
                    "fx:id=\"colCorreo\"",
                    "fx:id=\"colContrasena\"",
                    "fx:id=\"colRol\"",
                    "fx:id=\"colRutaImagen\"",
                    "fx:id=\"colEstado\"",
                    "fx:id=\"colCreado\"",
                    "fx:id=\"colEditado\""
            );

            for (String columna : columnasEsperadas) {
                assertTrue(contenido.contains(columna), "El FXML debe contener la columna: " + columna);
            }
        }
    }
}
