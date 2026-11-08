package com.mycompany.cine.daos;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

/**
 * Utilidad centralizada para la gestión física de imágenes en el proyecto Cine (Actividad 3.9).
 * Permite copiar archivos seleccionados por el usuario a carpetas locales del proyecto,
 * recuperar imágenes para su visualización en JavaFX y eliminar los archivos del disco
 * cuando los registros correspondientes son borrados de la base de datos.
 */
public final class GestorImagenes {

    // Directorio base de almacenamiento de imágenes dentro del proyecto
    public static final String CARPETA_RAIZ_IMAGENES = "imagenes";
    public static final String IMAGEN_POR_DEFECTO_USUARIO = "default_user.png";
    public static final String IMAGEN_POR_DEFECTO_PELICULA = "default_movie.png";

    private GestorImagenes() {
        // Clase utilitaria sin instanciación
    }

    /**
     * Copia un archivo de imagen externo a una carpeta interna del proyecto con un nombre único.
     *
     * @param archivoOrigen archivo físico seleccionado con FileChooser
     * @param subcarpeta    subdirectorio (ej. "administradores", "peliculas")
     * @param prefijo       prefijo identificador (ej. "admin_5", "pelicula_10")
     * @return ruta relativa guardada (ej. "imagenes/administradores/admin_5_1731045000.png")
     *         o la ruta original/defecto si ocurre un error
     */
    public static String copiarImagenAlProyecto(File archivoOrigen, String subcarpeta, String prefijo) {
        if (archivoOrigen == null || !archivoOrigen.exists() || !archivoOrigen.isFile()) {
            return IMAGEN_POR_DEFECTO_USUARIO;
        }

        try {
            // Asegurar que el directorio de destino exista
            Path carpetaDestino = Paths.get(CARPETA_RAIZ_IMAGENES, subcarpeta);
            if (!Files.exists(carpetaDestino)) {
                Files.createDirectories(carpetaDestino);
            }

            // Obtener la extensión del archivo original
            String nombreOriginal = archivoOrigen.getName();
            String extension = "";
            int punto = nombreOriginal.lastIndexOf('.');
            if (punto >= 0) {
                extension = nombreOriginal.substring(punto).toLowerCase();
            } else {
                extension = ".png";
            }

            // Generar un nombre único basado en prefijo y timestamp
            String nombreSanitizado = (prefijo != null && !prefijo.trim().isEmpty())
                    ? prefijo.replaceAll("[^a-zA-Z0-9_-]", "_")
                    : "img";
            long timestamp = Instant.now().toEpochMilli();
            String nombreDestino = nombreSanitizado + "_" + timestamp + extension;

            Path rutaDestino = carpetaDestino.resolve(nombreDestino);

            // Copiar reemplazando si ya existiera
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Devolver la ruta relativa normalizada con diagonales estándar
            return CARPETA_RAIZ_IMAGENES + "/" + subcarpeta + "/" + nombreDestino;

        } catch (IOException e) {
            System.err.println("Error al copiar imagen al proyecto: " + e.getMessage());
            return archivoOrigen.getAbsolutePath();
        }
    }

    /**
     * Elimina físicamente el archivo de imagen del disco cuando el registro se borra de la BD.
     * No borra imágenes por defecto (placeholders).
     *
     * @param rutaRelativaOAbsoluta ruta de la imagen registrada en la base de datos
     * @return true si se eliminó físicamente, false si no existía o era la imagen por defecto
     */
    public static boolean eliminarImagenDelProyecto(String rutaRelativaOAbsoluta) {
        if (rutaRelativaOAbsoluta == null || rutaRelativaOAbsoluta.trim().isEmpty()) {
            return false;
        }

        String ruta = rutaRelativaOAbsoluta.trim();

        // Evitar borrar placeholders predeterminados
        if (ruta.endsWith(IMAGEN_POR_DEFECTO_USUARIO) || ruta.endsWith(IMAGEN_POR_DEFECTO_PELICULA)) {
            return false;
        }

        try {
            Path path = Paths.get(ruta);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return Files.deleteIfExists(path);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen física del proyecto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Carga una imagen para JavaFX resolviendo tanto rutas relativas del proyecto,
     * rutas absolutas o recursos empaquetados.
     *
     * @param ruta ruta de la imagen
     * @return objeto {@link Image} listo para un {@link javafx.scene.image.ImageView}, o null si no existe
     */
    public static Image cargarImagen(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }

        try {
            File archivo = new File(ruta.trim());
            if (archivo.exists() && archivo.isFile()) {
                return new Image(archivo.toURI().toString());
            }

            // Buscar en el classpath de recursos
            var resource = GestorImagenes.class.getResource("/" + ruta.trim());
            if (resource != null) {
                return new Image(resource.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen en JavaFX: " + e.getMessage());
        }
        return null;
    }
}
