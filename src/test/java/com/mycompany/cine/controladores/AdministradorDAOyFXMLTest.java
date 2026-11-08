package com.mycompany.cine.controladores;

import com.mycompany.cine.daos.AdministradorDAO;
import com.mycompany.cine.daos.ConexionBD;
import com.mycompany.cine.modelo.Administrador;
import com.mycompany.cine.modelo.EEstados;
import com.mycompany.cine.modelo.ERoles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class AdministradorDAOyFXMLTest {

    @Test
    @DisplayName("El archivo administrador.fxml debe existir y contener los identificadores requeridos")
    public void testEstructuraFXML() throws Exception {
        InputStream is = getClass().getResourceAsStream("/com/mycompany/cine/administrador.fxml");
        assertNotNull(is, "El archivo administrador.fxml debe ser localizable en el classpath de recursos");

        String fxml = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(fxml.contains("fx:controller=\"com.mycompany.cine.controladores.AdministradorController\""),
                "El FXML debe asociarse a com.mycompany.cine.controladores.AdministradorController");

        // Componentes del formulario y botones CRUD
        assertTrue(fxml.contains("fx:id=\"txtId\""));
        assertTrue(fxml.contains("fx:id=\"txtNombre\""));
        assertTrue(fxml.contains("fx:id=\"txtEmail\""));
        assertTrue(fxml.contains("fx:id=\"txtUsuario\""));
        assertTrue(fxml.contains("fx:id=\"txtClaveAcceso\""));
        assertTrue(fxml.contains("fx:id=\"cmbRol\""));
        assertTrue(fxml.contains("fx:id=\"cmbEstado\""));
        assertTrue(fxml.contains("fx:id=\"txtRutaImagen\""));
        assertTrue(fxml.contains("fx:id=\"imgVistaPrevia\""));

        assertTrue(fxml.contains("fx:id=\"btnGuardar\""));
        assertTrue(fxml.contains("fx:id=\"btnActualizar\""));
        assertTrue(fxml.contains("fx:id=\"btnEliminar\""));
        assertTrue(fxml.contains("fx:id=\"btnLimpiar\""));

        // Navegación bidireccional
        assertTrue(fxml.contains("onAction=\"#handleVolverTaquilla\""));
        assertTrue(fxml.contains("onAction=\"#handleSalirLogin\""));

        // Columnas de la tabla
        assertTrue(fxml.contains("fx:id=\"tblAdministradores\""));
        assertTrue(fxml.contains("fx:id=\"colId\""));
        assertTrue(fxml.contains("fx:id=\"colNombre\""));
        assertTrue(fxml.contains("fx:id=\"colEmail\""));
        assertTrue(fxml.contains("fx:id=\"colUsuario\""));
        assertTrue(fxml.contains("fx:id=\"colRol\""));
        assertTrue(fxml.contains("fx:id=\"colEstado\""));
        assertTrue(fxml.contains("fx:id=\"colRutaImagen\""));
    }

    @Test
    @DisplayName("El DAO debe inicializarse correctamente y gestionar datos de Administrador")
    public void testInicializacionDAO() {
        AdministradorDAO dao = new AdministradorDAO();
        assertNotNull(dao);

        Administrador admin = new Administrador(
                100, "user_demo", "Carlos", "López", "carlos@cine.com",
                "demo123", ERoles.ADMINISTRADOR, EEstados.ACTIVO, "img/carlos.png"
        );

        assertEquals("user_demo", admin.getUsuario());
        assertEquals("Carlos", admin.getNombre());
        assertEquals("Carlos", admin.getNombres());
        assertEquals("López", admin.getApellido());
        assertEquals("López", admin.getApellidos());
        assertEquals("carlos@cine.com", admin.getEmail());
        assertEquals("carlos@cine.com", admin.getCorreo());
        assertEquals("Administrador", admin.getRol());
        assertEquals(ERoles.ADMINISTRADOR, admin.getRolEnum());
        assertEquals("Activo", admin.getEstado());
        assertEquals(EEstados.ACTIVO, admin.getEstadoEnum());
    }

    @Test
    @DisplayName("Verificar que los enumeradores ERoles y EEstados contengan las opciones válidas")
    public void testEnumsValores() {
        assertEquals(3, ERoles.values().length);
        assertEquals(ERoles.ADMINISTRADOR, ERoles.fromString("Administrador"));
        assertEquals(ERoles.GERENTE, ERoles.fromString("Gerente"));
        assertEquals(ERoles.SUPERVISOR, ERoles.fromString("Supervisor"));

        assertEquals(3, EEstados.values().length);
        assertEquals(EEstados.ACTIVO, EEstados.fromString("Activo"));
        assertEquals(EEstados.INACTIVO, EEstados.fromString("Inactivo"));
        assertEquals(EEstados.BLOQUEADO, EEstados.fromString("Bloqueado"));
    }
}
