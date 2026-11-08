/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Leonardo David Estrada Amador
 */
public final class Supervisor extends Persona {
    private String codigoAutorizacion;
    private BigDecimal limiteDescuento;
    private String estadoSupervisor;

    /**
     * Constructor vacío por defecto.
     * Requerido para la compatibilidad con frameworks e instanciación por defecto.
     */
    public Supervisor() {
        super();
    }

    /**
     * Constructor completo con los atributos indispensables para garantizar
     * que el objeto nazca en un estado válido e íntegro.
     *
     * @param id                 Identificador único primario del usuario.
     * @param nombre             Nombre completo del supervisor.
     * @param email              Correo electrónico institucional.
     * @param contrasena         Contraseña de acceso al sistema.
     * @param estado             Estado general de la cuenta (Activo, Inactivo, Bloqueado).
     * @param codigoAutorizacion Código único de autorización para operaciones especiales.
     * @param limiteDescuento    Límite máximo de descuento permitido.
     * @param estadoSupervisor   Estado operativo del supervisor (Disponible, En Auditoría).
     */
    public Supervisor(int id, String nombre, String email, String contrasena, String estado,
                      String codigoAutorizacion, BigDecimal limiteDescuento, String estadoSupervisor) {
        // Persona no tiene campo apellido separado; se usa el nombre como apellido
        // para que el correo quede en el campo email correctamente.
        super(id, nombre, nombre, email, contrasena, estado);
        setCodigoAutorizacion(codigoAutorizacion);
        setLimiteDescuento(limiteDescuento);
        setEstadoSupervisor(estadoSupervisor);
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public void setCodigoAutorizacion(String codigoAutorizacion) {
        if (codigoAutorizacion == null || codigoAutorizacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de autorización no puede ser nulo ni estar vacío.");
        }
        this.codigoAutorizacion = codigoAutorizacion.trim();
    }

    public BigDecimal getLimiteDescuento() {
        return limiteDescuento;
    }

    public void setLimiteDescuento(BigDecimal limiteDescuento) {
        if (limiteDescuento == null || limiteDescuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El límite de descuento no puede ser un valor negativo.");
        }
        this.limiteDescuento = limiteDescuento;
    }

    public String getEstadoSupervisor() {
        return estadoSupervisor;
    }

    public void setEstadoSupervisor(String estadoSupervisor) {
        if (estadoSupervisor == null || estadoSupervisor.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del supervisor no puede ser nulo ni estar vacío.");
        }
        this.estadoSupervisor = estadoSupervisor.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Supervisor supervisor = (Supervisor) obj;
        return getId() == supervisor.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", Supervisor{codigoAutorizacion='%s', limiteDescuento=%s, estadoSupervisor='%s'}",
                codigoAutorizacion,
                limiteDescuento != null ? limiteDescuento.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                estadoSupervisor);
    }
}
