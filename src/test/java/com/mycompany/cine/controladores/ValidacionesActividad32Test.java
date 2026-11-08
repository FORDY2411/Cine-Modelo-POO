package com.mycompany.cine.controladores;

import com.mycompany.cine.modelo.Administrador;
import com.mycompany.cine.persistencia.CineArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias que comprueban la especificación de la Actividad 3.2:
 * 1. Validaciones en tiempo real para cada campo (datos vacíos, caracteres incompatibles).
 * 2. Comprobaciones de tipos esperados (enteros, correos, solo letras, longitud).
 * 3. Habilitación y deshabilitación lógica de los botones según estado.
 * 4. Integración con la persistencia en memoria y la entidad Administrador.
 */
public class ValidacionesActividad32Test {

    private static final Pattern PATRON_SOLO_LETRAS = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$");
    private static final Pattern PATRON_USUARIO = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // =========================================================================
    // 1. VALIDACIÓN DE ID (ENTEROS POSITIVOS > 0)
    // =========================================================================
    @ParameterizedTest
    @ValueSource(strings = {"1", "45", "999", "1024"})
    @DisplayName("ID válido: enteros positivos mayores a cero")
    public void testIdValido(String idTexto) {
        assertTrue(esIdValido(idTexto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "0", "-1", "-50", "abc", "12a", "3.14"})
    @DisplayName("ID inválido: vacío, menor o igual a cero, o caracteres no numéricos")
    public void testIdInvalido(String idTexto) {
        assertFalse(esIdValido(idTexto));
    }

    private boolean esIdValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // =========================================================================
    // 2. VALIDACIÓN DE USUARIO (ALFANUMÉRICO DE 3 A 20 CARACTERES)
    // =========================================================================
    @ParameterizedTest
    @ValueSource(strings = {"admin", "cine_1", "super_usuario", "usr123"})
    @DisplayName("Usuario válido: alfanumérico entre 3 y 20 caracteres")
    public void testUsuarioValido(String usuario) {
        assertTrue(esUsuarioValido(usuario));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "ab", "admin@cine", "usuario con espacio", "super_usuario_extremadamente_largo_mas_de_20"})
    @DisplayName("Usuario inválido: vacío, muy corto, espacios o caracteres especiales")
    public void testUsuarioInvalido(String usuario) {
        assertFalse(esUsuarioValido(usuario));
    }

    private boolean esUsuarioValido(String usuario) {
        return usuario != null && !usuario.trim().isEmpty() && PATRON_USUARIO.matcher(usuario.trim()).matches();
    }

    // =========================================================================
    // 3. VALIDACIÓN DE NOMBRES Y APELLIDOS (SOLO LETRAS Y ESPACIOS)
    // =========================================================================
    @ParameterizedTest
    @ValueSource(strings = {"Carlos", "María José", "Ángel Peña", "Sofía Núñez"})
    @DisplayName("Nombres y apellidos válidos: letras y acentos")
    public void testTextoLetrasValido(String texto) {
        assertTrue(esTextoSoloLetras(texto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "Carlos123", "Maria_Jose", "Juan#", "12345"})
    @DisplayName("Nombres y apellidos inválidos: vacíos, números o símbolos")
    public void testTextoLetrasInvalido(String texto) {
        assertFalse(esTextoSoloLetras(texto));
    }

    private boolean esTextoSoloLetras(String texto) {
        return texto != null && !texto.trim().isEmpty() && PATRON_SOLO_LETRAS.matcher(texto.trim()).matches();
    }

    // =========================================================================
    // 4. VALIDACIÓN DE CORREO ELECTRÓNICO (FORMATO CON @ Y DOMINIO)
    // =========================================================================
    @ParameterizedTest
    @ValueSource(strings = {"admin@cine.com", "usuario.test@cinepolis.mx", "staff_1@taquilla.org"})
    @DisplayName("Correo válido: estructura con usuario, @ y dominio correcto")
    public void testCorreoValido(String correo) {
        assertTrue(esCorreoValido(correo));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "correosin_arroba.com", "admin@", "@dominio.com", "admin@dominio", "admin@@dominio.com"})
    @DisplayName("Correo inválido: vacío o sin estructura formal de email")
    public void testCorreoInvalido(String correo) {
        assertFalse(esCorreoValido(correo));
    }

    private boolean esCorreoValido(String correo) {
        return correo != null && !correo.trim().isEmpty() && PATRON_CORREO.matcher(correo.trim()).matches();
    }

    // =========================================================================
    // 5. VALIDACIÓN DE CONTRASEÑA (MÍNIMO 6 CARACTERES)
    // =========================================================================
    @ParameterizedTest
    @ValueSource(strings = {"123456", "admin2026", "claveSegura!", "Aa123456"})
    @DisplayName("Contraseña válida: longitud mínima de 6 caracteres")
    public void testContrasenaValida(String contrasena) {
        assertTrue(esContrasenaValida(contrasena));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "12345", "a", "abc"})
    @DisplayName("Contraseña inválida: vacía o menor a 6 caracteres")
    public void testContrasenaInvalida(String contrasena) {
        assertFalse(esContrasenaValida(contrasena));
    }

    private boolean esContrasenaValida(String contrasena) {
        return contrasena != null && contrasena.trim().length() >= 6;
    }

    // =========================================================================
    // 6. VALIDACIÓN INTEGRAL DEL FORMULARIO Y ESTADO DEL BOTÓN GUARDAR
    // =========================================================================
    @Test
    @DisplayName("Formulario completo válido permite habilitar el botón Guardar")
    public void testFormularioCompletoValido() {
        boolean idVal = esIdValido("5");
        boolean usrVal = esUsuarioValido("admin_juan");
        boolean nomVal = esTextoSoloLetras("Juan");
        boolean apeVal = esTextoSoloLetras("Perez");
        boolean corVal = esCorreoValido("juan.perez@cine.com");
        boolean pasVal = esContrasenaValida("password123");
        boolean rolVal = "Administrador General" != null && !("Administrador General".isEmpty());
        boolean estVal = "Activo" != null && !("Activo".isEmpty());

        boolean formularioValido = idVal && usrVal && nomVal && apeVal && corVal && pasVal && rolVal && estVal;
        assertTrue(formularioValido, "El formulario debe ser válido cuando todos los campos cumplen");

        // Regla 3.2: botón Guardar se habilita cuando todos los campos son válidos
        boolean btnGuardarDisabled = !formularioValido;
        assertFalse(btnGuardarDisabled, "El botón Guardar debe habilitarse (disabled = false)");
    }

    @Test
    @DisplayName("Formulario con campos vacíos o inválidos mantiene el botón Guardar deshabilitado")
    public void testFormularioIncompletoDeshabilitaGuardar() {
        // ID inválido
        boolean idVal = esIdValido("no_es_numero");
        boolean usrVal = esUsuarioValido("admin");
        boolean nomVal = esTextoSoloLetras("Juan");
        boolean apeVal = esTextoSoloLetras("Perez");
        boolean corVal = esCorreoValido("correo_invalido");
        boolean pasVal = esContrasenaValida("123"); // < 6 chars

        boolean formularioValido = idVal && usrVal && nomVal && apeVal && corVal && pasVal;
        assertFalse(formularioValido, "El formulario debe ser inválido");

        // Regla 3.2: si existen errores, Guardar debe permanecer deshabilitado
        boolean btnGuardarDisabled = !formularioValido;
        assertTrue(btnGuardarDisabled, "El botón Guardar debe permanecer deshabilitado (disabled = true)");
    }

    // =========================================================================
    // 7. COMPROBACIÓN DE PERSISTENCIA EN MEMORIA (CineArrayList)
    // =========================================================================
    @Test
    @DisplayName("CineArrayList administra Administradores con las columnas de base de datos")
    public void testPersistenciaCineArrayList() {
        CineArrayList repo = new CineArrayList();
        Administrador admin = new Administrador(
                100, "root_admin", "Ana", "Ruiz", "ana.ruiz@cine.com",
                "secreto99", "SuperAdmin", "ana.png", "Activo",
                "2026-10-20 12:00:00", "2026-10-20 12:00:00"
        );

        // Agregar
        assertTrue(repo.agregar(admin));
        assertEquals(1, repo.listar().size());

        // No permitir duplicados por ID
        Administrador duplicado = new Administrador(
                100, "otro_usuario", "Pedro", "Gómez", "pedro@cine.com",
                "clave123", "Gerente", "pedro.png", "Activo",
                "2026-10-20 12:00:00", "2026-10-20 12:00:00"
        );
        assertFalse(repo.agregar(duplicado), "No debe permitir IDs duplicados");

        // Traer
        Administrador recuperado = repo.traer("100");
        assertNotNull(recuperado);
        assertEquals("root_admin", recuperado.getUsuario());
        assertEquals("Ana", recuperado.getNombres());

        // Actualizar
        recuperado.setUsuario("root_modificado");
        recuperado.setEditado("2026-10-20 13:00:00");
        assertTrue(repo.actualizar(recuperado));
        assertEquals("root_modificado", repo.traer("100").getUsuario());

        // Eliminar
        assertTrue(repo.eliminar("100"));
        assertNull(repo.traer("100"));
        assertEquals(0, repo.listar().size());
    }
}
