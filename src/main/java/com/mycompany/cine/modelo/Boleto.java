/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Fordiis
 */
public class Boleto {
    private int idBoleto;
    private Funcion funcion;
    private String numeroAsiento;
    private Persona cliente;
    private BigDecimal precioFinal;
    private String metodoPago;
    private String estadoBoleto;

    /**
     * Constructor vacío por defecto.
     */
    public Boleto() {
    }

    /**
     * Constructor completo para garantizar un boleto emitido en estado válido.
     *
     * @param idBoleto     Identificador único del boleto.
     * @param funcion      Función asignada.
     * @param numeroAsiento Código de asiento (ej. "F12").
     * @param cliente      Instancia de Persona (o subclase) que adquiere el boleto.
     * @param precioFinal  Precio calculado final tras promociones/descuentos.
     * @param metodoPago   Forma de pago (Efectivo, Tarjeta, Puntos).
     * @param estadoBoleto Estado (Emitido, Usado, Cancelado, Reembolsado).
     */
    public Boleto(int idBoleto, Funcion funcion, String numeroAsiento, Persona cliente,
                  BigDecimal precioFinal, String metodoPago, String estadoBoleto) {
        setIdBoleto(idBoleto);
        setFuncion(funcion);
        setNumeroAsiento(numeroAsiento);
        setCliente(cliente);
        setPrecioFinal(precioFinal);
        setMetodoPago(metodoPago);
        setEstadoBoleto(estadoBoleto);
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        if (idBoleto <= 0) {
            throw new IllegalArgumentException("El ID del boleto debe ser mayor a cero.");
        }
        this.idBoleto = idBoleto;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función asociada no puede ser nula.");
        }
        this.funcion = funcion;
    }

    public String getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(String numeroAsiento) {
        if (numeroAsiento == null || numeroAsiento.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de asiento no puede estar vacío.");
        }
        this.numeroAsiento = numeroAsiento.trim();
    }

    public Persona getCliente() {
        return cliente;
    }

    public void setCliente(Persona cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente asignado no puede ser nulo.");
        }
        this.cliente = cliente;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        if (precioFinal == null || precioFinal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio final no puede ser negativo.");
        }
        this.precioFinal = precioFinal;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío.");
        }
        this.metodoPago = metodoPago.trim();
    }

    public String getEstadoBoleto() {
        return estadoBoleto;
    }

    public void setEstadoBoleto(String estadoBoleto) {
        if (estadoBoleto == null || estadoBoleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del boleto no puede estar vacío.");
        }
        this.estadoBoleto = estadoBoleto.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Boleto boleto = (Boleto) obj;
        return idBoleto == boleto.idBoleto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBoleto);
    }

    @Override
    public String toString() {
        return String.format(
                "Boleto{idBoleto=%d, funcionId=%s, numeroAsiento='%s', cliente=%s, precioFinal=%s, metodoPago='%s', estadoBoleto='%s'}",
                idBoleto,
                funcion != null ? funcion.getIdFuncion() : "N/A",
                numeroAsiento,
                cliente != null ? cliente.getNombre() : "N/A",
                precioFinal != null ? precioFinal.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() : "null",
                metodoPago, estadoBoleto);
    }
}
