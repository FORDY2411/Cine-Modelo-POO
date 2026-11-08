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
public class Sala {
    private int idSala;
    private int numero;
    private int capacidad;
    private String tecnologia;
    private String estadoSala;

    /**
     * Constructor vacío por defecto.
     */
    public Sala() {
    }

    /**
     * Constructor completo con atributos mínimos para la creación de una sala.
     * 
     * @param idSala Identificador único de la sala.
     * @param numero Número de sala asignado.
     * @param capacidad Capacidad máxima de butacas.
     * @param tecnologia Tipo de pantalla/sonido (ej. "2D", "3D", "IMAX", "4DX").
     * @param estadoSala Estado de disponibilidad (Disponible, Mantenimiento, Limpieza).
     */
    public Sala(int idSala, int numero, int capacidad, String tecnologia, String estadoSala) {
        setIdSala(idSala);
        setNumero(numero);
        setCapacidad(capacidad);
        setTecnologia(tecnologia);
        setEstadoSala(estadoSala);
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        if (idSala <= 0) {
            throw new IllegalArgumentException("El ID de la sala debe ser mayor a cero.");
        }
        this.idSala = idSala;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de sala debe ser positivo.");
        }
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        this.capacidad = capacidad;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        if (tecnologia == null || tecnologia.trim().isEmpty()) {
            throw new IllegalArgumentException("La tecnología no puede estar vacía.");
        }
        this.tecnologia = tecnologia.trim();
    }

    public String getEstadoSala() {
        return estadoSala;
    }

    public void setEstadoSala(String estadoSala) {
        if (estadoSala == null || estadoSala.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la sala no puede estar vacío.");
        }
        this.estadoSala = estadoSala.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sala sala = (Sala) obj;
        return idSala == sala.idSala;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSala);
    }

    @Override
    public String toString() {
        return "Sala " + numero + " (" + (tecnologia != null ? tecnologia : "") + ")";
    }
}
