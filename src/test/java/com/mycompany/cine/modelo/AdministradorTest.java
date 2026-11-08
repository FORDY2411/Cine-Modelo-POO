package com.mycompany.cine.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Administrador verificando todas las columnas
 * correspondientes a la tabla 'administradores' de la base de datos MySQL:
 * id, usuario, nombres, apellidos, correo, contrasena, rol, ruta_imagen, estado, creado, editado.
 */
public class AdministradorTest {

    @Test
    @DisplayName("Debe instanciar Administrador con todas las 11 columnas de la base de datos")
    public void testConstructorColumnasBaseDeDatos() {
        Administrador admin = new Administrador(
                10,
                "admin_central",
                "Juan Carlos",
                "Pérez Morales",
                "juan.perez@cine.com",
                "secreto123",
                "SuperAdmin",
                "fotos/admin10.png",
                "Activo",
                "2026-10-20 10:00:00",
                "2026-10-20 12:30:00"
        );

        // Verificación de columnas de BD
        assertEquals(10, admin.getId(), "El ID debe coincidir");
        assertEquals("admin_central", admin.getUsuario(), "La columna 'usuario' debe coincidir");
        assertEquals("Juan Carlos", admin.getNombres(), "La columna 'nombres' debe coincidir");
        assertEquals("Juan Carlos", admin.getNombre(), "getNombre() debe reflejar nombres");
        assertEquals("Pérez Morales", admin.getApellidos(), "La columna 'apellidos' debe coincidir");
        assertEquals("Pérez Morales", admin.getApellido(), "getApellido() debe reflejar apellidos");
        assertEquals("juan.perez@cine.com", admin.getCorreo(), "La columna 'correo' debe coincidir");
        assertEquals("juan.perez@cine.com", admin.getEmail(), "getEmail() debe reflejar correo");
        assertEquals("secreto123", admin.getContrasena(), "La columna 'contrasena' debe coincidir");
        assertEquals("SuperAdmin", admin.getRol(), "La columna 'rol' debe coincidir");
        assertEquals("SuperAdmin", admin.getNivelAcceso(), "getNivelAcceso() debe reflejar rol");
        assertEquals("fotos/admin10.png", admin.getRutaImagen(), "La columna 'ruta_imagen' debe coincidir");
        assertEquals("Activo", admin.getEstado(), "La columna 'estado' debe coincidir");
        assertEquals("2026-10-20 10:00:00", admin.getCreado(), "La columna 'creado' debe coincidir");
        assertEquals("2026-10-20 12:30:00", admin.getEditado(), "La columna 'editado' debe coincidir");
    }

    @Test
    @DisplayName("Debe actualizar correctamente las propiedades mediante setters")
    public void testSettersPropiedades() {
        Administrador admin = new Administrador();
        admin.setId(1);
        admin.setUsuario("admin_mod");
        admin.setNombres("Laura");
        admin.setApellidos("Hernández");
        admin.setCorreo("laura@cine.com");
        admin.setContrasena("nuevaClave789");
        admin.setRol("Gerente General");
        admin.setRutaImagen("img/laura.jpg");
        admin.setEstado("Inactivo");
        admin.setCreado("2026-10-20 08:00:00");
        admin.setEditado("2026-10-20 15:45:00");

        assertEquals(1, admin.getId());
        assertEquals("admin_mod", admin.getUsuario());
        assertEquals("Laura", admin.getNombres());
        assertEquals("Hernández", admin.getApellidos());
        assertEquals("laura@cine.com", admin.getCorreo());
        assertEquals("nuevaClave789", admin.getContrasena());
        assertEquals("Gerente General", admin.getRol());
        assertEquals("img/laura.jpg", admin.getRutaImagen());
        assertEquals("Inactivo", admin.getEstado());
        assertEquals("2026-10-20 08:00:00", admin.getCreado());
        assertEquals("2026-10-20 15:45:00", admin.getEditado());
    }

    @Test
    @DisplayName("Debe permitir instanciación con ID = 0 para nuevas inserciones en MySQL (AUTO_INCREMENT)")
    public void testIdCeroPermitidoParaNuevosRegistros() {
        assertDoesNotThrow(() -> {
            Administrador nuevo = new Administrador(
                    0, "nuevo_user", "Mario", "Bros", "mario@cine.com",
                    "clave123", "Administrador", "default_user.png", "Activo", null, null
            );
            assertEquals(0, nuevo.getId());
            assertEquals("nuevo_user", nuevo.getUsuario());
        });
    }

    @Test
    @DisplayName("Debe conservar compatibilidad con constructor heredado de 5 parámetros")
    public void testCompatibilidadConstructorPrevio() {
        Administrador admin = new Administrador("Supervisor", 5, "Pedro", "García", "pedro@cine.com");
        assertEquals(5, admin.getId());
        assertEquals("Pedro", admin.getNombre());
        assertEquals("Pedro", admin.getNombres());
        assertEquals("García", admin.getApellido());
        assertEquals("García", admin.getApellidos());
        assertEquals("pedro@cine.com", admin.getEmail());
        assertEquals("pedro@cine.com", admin.getCorreo());
        assertEquals("Supervisor", admin.getNivelAcceso());
        assertEquals("Supervisor", admin.getRol());
    }
}
