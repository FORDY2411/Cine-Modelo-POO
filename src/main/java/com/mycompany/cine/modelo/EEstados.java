package com.mycompany.cine.modelo;

/**
 * Enumerador de estados para los usuarios y administradores del sistema,
 * coincidente con el ENUM('Activo', 'Inactivo', 'Bloqueado')
 * de la base de datos MySQL.
 */
public enum EEstados {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    BLOQUEADO("Bloqueado");

    private final String descripcion;

    EEstados(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static EEstados fromString(String valor) {
        if (valor == null) return ACTIVO;
        for (EEstados estado : EEstados.values()) {
            if (estado.name().equalsIgnoreCase(valor.trim()) || estado.descripcion.equalsIgnoreCase(valor.trim())) {
                return estado;
            }
        }
        return ACTIVO;
    }
}
