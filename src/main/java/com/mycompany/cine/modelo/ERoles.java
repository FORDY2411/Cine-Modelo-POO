package com.mycompany.cine.modelo;

/**
 * Enumerador de roles para los administradores del sistema,
 * coincidente con el ENUM('Administrador', 'Gerente', 'Supervisor')
 * de la base de datos MySQL.
 */
public enum ERoles {
    ADMINISTRADOR("Administrador"),
    GERENTE("Gerente"),
    SUPERVISOR("Supervisor");

    private final String descripcion;

    ERoles(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static ERoles fromString(String valor) {
        if (valor == null) return ADMINISTRADOR;
        for (ERoles rol : ERoles.values()) {
            if (rol.name().equalsIgnoreCase(valor.trim()) || rol.descripcion.equalsIgnoreCase(valor.trim())) {
                return rol;
            }
        }
        return ADMINISTRADOR;
    }
}
