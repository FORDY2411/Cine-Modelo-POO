
package com.mycompany.cine.modelo;

import java.util.Objects;

/**
 * Clase base abstracta que representa a cualquier persona que interactúa con
 * el sistema de gestión de ventas del cine: personal administrativo
 * (administrador, gerente, supervisor) y clientes (fan, megafan).
 * <p>
 * Al ser abstracta no puede instanciarse directamente; solo sus subclases
 * concretas generan objetos. Centraliza los datos y el comportamiento comunes
 * (identidad, datos de contacto, contraseña, estado y activo), de modo que las
 * subclases solo agregan lo propio de su rol.
 *
 * @author Leonardo David
 */
public abstract class Persona {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String estado;
    private boolean activo;

    /**
     * Constructor vacío. Crea una persona sin datos asignados; permite la
     * instanciación por defecto y la compatibilidad con frameworks.
     */
    public Persona() {
        this.activo = true;
        this.estado = "Activo";
    }

    /**
     * Constructor con los datos mínimos indispensables para que la persona
     * nazca en un estado válido. La persona se crea activa por defecto.
     *
     * @param id      identificador único; debe ser un entero mayor que 0
     * @param nombre  nombre de la persona; no puede ser nulo ni vacío
     * @param apellido apellido de la persona; no puede ser nulo ni vacío
     * @param email   correo electrónico; debe contener el carácter '@'
     * @throws IllegalArgumentException si algún dato no cumple su validación
     */
    public Persona(int id, String nombre, String apellido, String email) {
        this(id, nombre, apellido, email, null, "Activo");
    }

    /**
     * Constructor completo con datos de acceso y estado de cuenta.
     *
     * @param id        identificador único; debe ser un entero mayor que 0
     * @param nombre    nombre de la persona; no puede ser nulo ni vacío
     * @param apellido  apellido de la persona; no puede ser nulo ni vacío
     * @param email     correo electrónico; debe contener el carácter '@'
     * @param contrasena contraseña de acceso (puede ser {@code null})
     * @param estado    estado general de la cuenta (ej. Activo, Inactivo, Bloqueado)
     * @throws IllegalArgumentException si algún dato obligatorio no cumple su validación
     */
    public Persona(int id, String nombre, String apellido, String email,
                   String contrasena, String estado) {
        setId(id);
        setNombre(nombre);
        setApellido(apellido);
        setEmail(email);
        setContrasena(contrasena);
        setEstado(estado);
        this.activo = true;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombres() {
        return getNombre();
    }

    public void setNombres(String nombres) {
        setNombre(nombres);
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            this.apellido = (this.nombre != null && !this.nombre.trim().isEmpty()) ? this.nombre : "Sin Apellido";
        } else {
            this.apellido = apellido.trim();
        }
    }

    public String getApellidos() {
        return getApellido();
    }

    public void setApellidos(String apellidos) {
        setApellido(apellidos);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        this.email = email.trim();
    }

    public String getCorreo() {
        return getEmail();
    }

    public void setCorreo(String correo) {
        setEmail(correo);
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = (estado == null || estado.trim().isEmpty()) ? "Activo" : estado.trim();
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Persona other = (Persona) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id
                + ", nombre=" + nombre
                + ", apellido=" + apellido
                + ", email=" + email
                + ", estado=" + estado
                + ", activo=" + activo + '}';
    }
}
