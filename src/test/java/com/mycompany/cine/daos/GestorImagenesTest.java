package com.mycompany.cine.daos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GestorImagenesTest {

    @Test
    @DisplayName("Debe devolver imagen por defecto si el archivo origen es nulo o no existe")
    public void testArchivoInvalidoDevuelveDefault() {
        String resultadoNull = GestorImagenes.copiarImagenAlProyecto(null, "administradores", "test");
        assertEquals(GestorImagenes.IMAGEN_POR_DEFECTO_USUARIO, resultadoNull);

        File noExiste = new File("archivo_inexistente_xyz.png");
        String resultadoNoExiste = GestorImagenes.copiarImagenAlProyecto(noExiste, "administradores", "test");
        assertEquals(GestorImagenes.IMAGEN_POR_DEFECTO_USUARIO, resultadoNoExiste);
    }

    @Test
    @DisplayName("Debe copiar un archivo real a la carpeta de destino y luego eliminarlo")
    public void testCopiarYEliminarImagen(@TempDir Path tempDir) throws IOException {
        // Crear archivo de prueba simulado
        Path archivoPrueba = tempDir.resolve("foto_prueba.png");
        Files.writeString(archivoPrueba, "contenido simulado de imagen");

        String rutaCopiada = GestorImagenes.copiarImagenAlProyecto(archivoPrueba.toFile(), "test_dir", "usuario_demo");

        assertNotNull(rutaCopiada);
        assertTrue(rutaCopiada.startsWith("imagenes/test_dir/"));
        assertTrue(rutaCopiada.endsWith(".png"));

        File archivoCopiado = new File(rutaCopiada);
        assertTrue(archivoCopiado.exists(), "El archivo físico copiado debe existir en el proyecto");

        // Probar eliminación física
        boolean eliminado = GestorImagenes.eliminarImagenDelProyecto(rutaCopiada);
        assertTrue(eliminado, "Debe retornar true al eliminar el archivo existente");
        assertFalse(archivoCopiado.exists(), "El archivo ya no debe existir en disco");
    }

    @Test
    @DisplayName("No debe eliminar la imagen por defecto placeholder")
    public void testNoEliminarPlaceholder() {
        boolean eliminado = GestorImagenes.eliminarImagenDelProyecto(GestorImagenes.IMAGEN_POR_DEFECTO_USUARIO);
        assertFalse(eliminado, "No debe permitir borrar el placeholder default_user.png");
    }
}
