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
public final class ClienteMegaFan extends Persona {

    private int puntosVIP;
    private boolean pasePrioritario;
    private BigDecimal porcentajeVIP;
    private String estadoClienteMegaFan;

    /**
     * Constructor vacío por defecto.
     * Requerido para la compatibilidad con frameworks e instanciación por defecto.
     */
    public ClienteMegaFan() {
        super();
    }

    /**
     * Constructor completo con los atributos indispensables para garantizar
     * que el objeto nazca en un estado válido e íntegro.
     *
     * @param id                    Identificador único primario del usuario.
     * @param nombre                Nombre completo del cliente VIP.
     * @param email                 Correo electrónico personal.
     * @param contrasena            Contraseña de acceso a la plataforma.
     * @param estado                Estado general de la cuenta (Activo, Inactivo, Bloqueado).
     * @param puntosVIP             Puntos exclusivos de categoría VIP acumulados.
     * @param pasePrioritario       Indica si cuenta con acceso preferencial a salas/taquillas.
     * @param porcentajeVIP         Porcentaje de descuento preferencial asignado.
     * @param estadoClienteMegaFan  Estado específico de la categoría VIP (VIP Activo, Suspendido, Degradado).
     */
    public ClienteMegaFan(int id, String nombre, String email, String contrasena, String estado,
                          int puntosVIP, boolean pasePrioritario, BigDecimal porcentajeVIP, String estadoClienteMegaFan) {
        // Persona no tiene campo apellido separado; se usa el nombre como apellido
        // para que el correo quede en el campo email correctamente.
        super(id, nombre, nombre, email, contrasena, estado);
        setPuntosVIP(puntosVIP);
        setPasePrioritario(pasePrioritario);
        setPorcentajeVIP(porcentajeVIP);
        setEstadoClienteMegaFan(estadoClienteMegaFan);
    }

    public int getPuntosVIP() {
        return puntosVIP;
    }

    public void setPuntosVIP(int puntosVIP) {
        if (puntosVIP < 0) {
            throw new IllegalArgumentException("Los puntos VIP no pueden ser un valor negativo.");
        }
        this.puntosVIP = puntosVIP;
    }

    public boolean isPasePrioritario() {
        return pasePrioritario;
    }

    public void setPasePrioritario(boolean pasePrioritario) {
        this.pasePrioritario = pasePrioritario;
    }

    public BigDecimal getPorcentajeVIP() {
        return porcentajeVIP;
    }

    public void setPorcentajeVIP(BigDecimal porcentajeVIP) {
        if (porcentajeVIP == null || porcentajeVIP.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El porcentaje VIP no puede ser un valor negativo.");
        }
        this.porcentajeVIP = porcentajeVIP;
    }

    public String getEstadoClienteMegaFan() {
        return estadoClienteMegaFan;
    }

    public void setEstadoClienteMegaFan(String estadoClienteMegaFan) {
        if (estadoClienteMegaFan == null || estadoClienteMegaFan.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del cliente MegaFan no puede ser nulo ni estar vacío.");
        }
        this.estadoClienteMegaFan = estadoClienteMegaFan.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteMegaFan clienteMegaFan = (ClienteMegaFan) obj;
        return getId() == clienteMegaFan.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", ClienteMegaFan{puntosVIP=%d, pasePrioritario=%b, porcentajeVIP=%s, estadoClienteMegaFan='%s'}",
                puntosVIP, pasePrioritario,
                porcentajeVIP != null ? porcentajeVIP.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                estadoClienteMegaFan);
    }
}
