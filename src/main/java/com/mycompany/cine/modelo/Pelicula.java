/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.util.Objects;

/**
 *
 * @author Fordiis
 */
public class Pelicula {

    private int idPelicula;
    private String titulo;
    private int duracion; // en minutos
    private String clasificacion;
    private String genero;
    private String estadoPelicula;
    private String rutaImagen;

    /**
     * Constructor vacío por defecto.
     * Requerido para la compatibilidad con frameworks e instanciación por defecto.
     */
    public Pelicula() {
    }

    /**
     * Constructor con atributos principales.
     */
    public Pelicula(int idPelicula, String titulo, int duracion, String clasificacion, String genero, String estadoPelicula) {
        this(idPelicula, titulo, duracion, clasificacion, genero, estadoPelicula, null);
    }

    /**
     * Constructor completo incluyendo ruta de la imagen / póster.
     */
    public Pelicula(int idPelicula, String titulo, int duracion, String clasificacion, String genero, String estadoPelicula, String rutaImagen) {
        setIdPelicula(idPelicula);
        setTitulo(titulo);
        setDuracion(duracion);
        setClasificacion(clasificacion);
        setGenero(genero);
        setEstadoPelicula(estadoPelicula);
        setRutaImagen(rutaImagen);
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        if (idPelicula < 0) {
            throw new IllegalArgumentException("El ID de la película no puede ser negativo.");
        }
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos.");
        }
        this.duracion = duracion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La clasificación no puede estar vacía.");
        }
        this.clasificacion = clasificacion.trim();
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío.");
        }
        this.genero = genero.trim();
    }

    public String getEstadoPelicula() {
        return estadoPelicula;
    }

    public void setEstadoPelicula(String estadoPelicula) {
        if (estadoPelicula == null || estadoPelicula.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la película no puede estar vacío.");
        }
        this.estadoPelicula = estadoPelicula.trim();
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = (rutaImagen != null && !rutaImagen.trim().isEmpty()) ? rutaImagen.trim() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pelicula pelicula = (Pelicula) obj;
        return idPelicula == pelicula.idPelicula;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPelicula);
    }

    @Override
    public String toString() {
        return (titulo != null && !titulo.isEmpty()) ? titulo : "Película #" + idPelicula;
    }
    
    
    
    
    
    
}
