/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Clase que representa a un Gerente de Taquilla en el sistema de gestión de cine.
 * Hereda de la clase base abstracta Persona e incorpora atributos específicos para
 * la administración de cajas y apertura de turnos.
 * Se declara como final para evitar derivaciones posteriores en la jerarquía.
 *
 * @author Leonardo David Estrada Amador
 * @version 1.0
 */
public final class Gerente extends Persona {

    private int idCaja;
    private BigDecimal montoApertura;
    private String estadoGerente;

    /**
     * Constructor vacío por defecto.
     * Requerido para la compatibilidad con frameworks e instanciación por defecto.
     */
    public Gerente() {
        super();
    }

    /**
     * Constructor completo con los atributos indispensables para garantizar
     * que el objeto nazca en un estado válido e íntegro.
     *
     * @param id            Identificador único primario del usuario.
     * @param nombre        Nombre completo del gerente.
     * @param email         Correo electrónico institucional.
     * @param contrasena    Contraseña de acceso al sistema.
     * @param estado        Estado general de la cuenta (Activo, Inactivo, Bloqueado).
     * @param idCaja        Identificador de la caja asignada.
     * @param montoApertura Monto inicial reportado al abrir caja.
     * @param estadoGerente Estado específico del rol (Caja Abierta, Caja Cerrada, En Arqueo).
     */
    public Gerente(int id, String nombre, String email, String contrasena, String estado,
                   int idCaja, BigDecimal montoApertura, String estadoGerente) {
        // Persona no tiene campo apellido separado; se usa el nombre como apellido
        // para que el correo quede en el campo email correctamente.
        super(id, nombre, nombre, email, contrasena, estado);
        setIdCaja(idCaja);
        setMontoApertura(montoApertura);
        setEstadoGerente(estadoGerente);
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        if (idCaja <= 0) {
            throw new IllegalArgumentException("El ID de la caja debe ser un entero positivo mayor a cero.");
        }
        this.idCaja = idCaja;
    }

    public BigDecimal getMontoApertura() {
        return montoApertura;
    }

    public void setMontoApertura(BigDecimal montoApertura) {
        if (montoApertura == null || montoApertura.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de apertura no puede ser un valor negativo.");
        }
        this.montoApertura = montoApertura;
    }

    public String getEstadoGerente() {
        return estadoGerente;
    }

    public void setEstadoGerente(String estadoGerente) {
        if (estadoGerente == null || estadoGerente.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del gerente no puede ser nulo ni estar vacío.");
        }
        this.estadoGerente = estadoGerente.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gerente gerente = (Gerente) obj;
        return getId() == gerente.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", Gerente{idCaja=%d, montoApertura=%s, estadoGerente='%s'}",
                idCaja,
                montoApertura != null ? montoApertura.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                estadoGerente);
    }
}
