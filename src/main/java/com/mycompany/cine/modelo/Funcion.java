/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Fordiis
 */
public class Funcion {
    private int idFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime fechaHora;
    private BigDecimal precioBase;
    private String estadoFuncion;

    /**
     * Constructor vacío por defecto.
     */
    public Funcion() {
    }

    /**
     * Constructor completo con los atributos requeridos para programar una función.
     *
     * @param idFuncion   Identificador único de la función.
     * @param pelicula    Objeto Pelicula asociada.
     * @param sala        Objeto Sala donde se proyectará.
     * @param fechaHora   Fecha y hora de la proyección.
     * @param precioBase  Precio base del boleto antes de descuentos.
     * @param estadoFuncion Estado (Programada, En Proyección, Finalizada, Agotada).
     */
    public Funcion(int idFuncion, Pelicula pelicula, Sala sala, LocalDateTime fechaHora,
                   BigDecimal precioBase, String estadoFuncion) {
        setIdFuncion(idFuncion);
        setPelicula(pelicula);
        setSala(sala);
        setFechaHora(fechaHora);
        setPrecioBase(precioBase);
        setEstadoFuncion(estadoFuncion);
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        if (idFuncion < 0) {
            throw new IllegalArgumentException("El ID de la función no puede ser negativo.");
        }
        this.idFuncion = idFuncion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película asignada no puede ser nula.");
        }
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        if (sala == null) {
            throw new IllegalArgumentException("La sala asignada no puede ser nula.");
        }
        this.sala = sala;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas.");
        }
        this.fechaHora = fechaHora;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        if (precioBase == null || precioBase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor a cero.");
        }
        this.precioBase = precioBase;
    }

    public String getEstadoFuncion() {
        return estadoFuncion;
    }

    public void setEstadoFuncion(String estadoFuncion) {
        if (estadoFuncion == null || estadoFuncion.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la función no puede estar vacío.");
        }
        this.estadoFuncion = estadoFuncion.trim();
    }

    public String getTituloPelicula() {
        return (pelicula != null) ? pelicula.getTitulo() : "";
    }

    public String getNombreSala() {
        return (sala != null) ? "Sala " + sala.getNumero() + " (" + sala.getTecnologia() + ")" : "";
    }

    public String getPrecioFormateado() {
        return (precioBase != null) ? "$" + precioBase.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "$0.00";
    }

    public String getFechaHoraFormateada() {
        return (fechaHora != null) ? fechaHora.toString().replace("T", " ") : "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Funcion funcion = (Funcion) obj;
        return idFuncion == funcion.idFuncion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFuncion);
    }

    @Override
    public String toString() {
        return String.format(
                "Funcion{idFuncion=%d, pelicula=%s, sala=%s, fechaHora=%s, precioBase=%s, estadoFuncion='%s'}",
                idFuncion,
                pelicula != null ? pelicula.getTitulo() : "N/A",
                sala != null ? sala.getNumero() : "N/A",
                fechaHora,
                precioBase != null ? precioBase.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                estadoFuncion);
    }
}
