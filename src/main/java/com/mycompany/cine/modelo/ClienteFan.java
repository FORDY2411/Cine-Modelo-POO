/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.math.BigDecimal;
import java.util.Objects;


//@autor Leonardo David Estrada Amador
public final class ClienteFan extends Persona {

    private int puntosAcumulados;
    private BigDecimal porcentajeBase;
    private String estadoClienteFan;

    /**
     * Constructor vacío por defecto.
     * Requerido para la compatibilidad con frameworks e instanciación por defecto.
     */
    public ClienteFan() {
        super();
    }

    /**
     * Constructor completo con los atributos indispensables para garantizar
     * que el objeto nazca en un estado válido e íntegro.
     *
     * @param id               Identificador único primario del usuario.
     * @param nombre           Nombre completo del cliente.
     * @param email            Correo electrónico personal.
     * @param contrasena       Contraseña de acceso a la plataforma.
     * @param estado           Estado general de la cuenta (Activo, Inactivo, Bloqueado).
     * @param puntosAcumulados Puntos acumulados por compras realizadas.
     * @param porcentajeBase   Porcentaje de descuento base asignado a la membresía.
     * @param estadoClienteFan Estado específico de la membresía (Membresía Activa, Vencida, Pendiente).
     */
    public ClienteFan(int id, String nombre, String email, String contrasena, String estado,
                      int puntosAcumulados, BigDecimal porcentajeBase, String estadoClienteFan) {
        // Persona no tiene campo apellido separado; se usa el nombre como apellido
        // para que el correo quede en el campo email correctamente.
        super(id, nombre, nombre, email, contrasena, estado);
        setPuntosAcumulados(puntosAcumulados);
        setPorcentajeBase(porcentajeBase);
        setEstadoClienteFan(estadoClienteFan);
    }

    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(int puntosAcumulados) {
        if (puntosAcumulados < 0) {
            throw new IllegalArgumentException("Los puntos acumulados no pueden ser un valor negativo.");
        }
        this.puntosAcumulados = puntosAcumulados;
    }

    public BigDecimal getPorcentajeBase() {
        return porcentajeBase;
    }

    public void setPorcentajeBase(BigDecimal porcentajeBase) {
        if (porcentajeBase == null || porcentajeBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El porcentaje base no puede ser un valor negativo.");
        }
        this.porcentajeBase = porcentajeBase;
    }

    public String getEstadoClienteFan() {
        return estadoClienteFan;
    }

    public void setEstadoClienteFan(String estadoClienteFan) {
        if (estadoClienteFan == null || estadoClienteFan.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del cliente fan no puede ser nulo ni estar vacío.");
        }
        this.estadoClienteFan = estadoClienteFan.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteFan clienteFan = (ClienteFan) obj;
        return getId() == clienteFan.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", ClienteFan{puntosAcumulados=%d, porcentajeBase=%s, estadoClienteFan='%s'}",
                puntosAcumulados,
                porcentajeBase != null ? porcentajeBase.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                estadoClienteFan);
    }
}
