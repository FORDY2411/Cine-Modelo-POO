package com.mycompany.cine.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entidad Administrador que extiende de Persona.
 * Representa los datos del personal administrativo y se mapea con la tabla
 * 'administradores' de la base de datos MySQL (id, usuario, nombres, apellidos, correo,
 * contrasena, rol, ruta_imagen, estado, creado, editado).
 */
public final class Administrador extends Persona {
    
    private static final List<String> ESTADOS_TURNO =
            Arrays.asList("En turno", "De licencia", "Ausente");
    
    private String nivelAcceso;
    private List<String> bitacora = new ArrayList<>();
    private String estadoTurno = "En turno";

    // Columnas de la tabla 'administradores'
    private String usuario;
    private String rol;
    private String rutaImagen;
    private String creado;
    private String editado;
    
    // Campos auxiliares opcionales para compatibilidad con guías didácticas
    private String telefono = "";
    private String direccion = "";

    public Administrador() {
        super();
    }

    public Administrador(String nivelAcceso, int id, String nombre, String apellido, String email) {
        super(id, nombre, apellido, email);
        this.nivelAcceso = nivelAcceso;
        this.rol = nivelAcceso;
    }

    /**
     * Constructor completo correspondiente a todas las columnas de la tabla 'administradores'.
     */
    public Administrador(int id, String usuario, String nombres, String apellidos, String correo,
                         String contrasena, String rol, String rutaImagen, String estado,
                         String creado, String editado) {
        super(id, nombres, apellidos, correo, contrasena, estado);
        this.usuario = usuario;
        this.rol = rol;
        this.nivelAcceso = rol;
        this.rutaImagen = rutaImagen;
        this.creado = creado;
        this.editado = editado;
    }

    /**
     * Constructor compatible con objetos tipo Enum y formularios directos CRUD.
     */
    public Administrador(int id, String usuario, String nombres, String apellidos, String correo,
                         String contrasena, ERoles rol, EEstados estado, String rutaImagen) {
        super(id, nombres, apellidos, correo, contrasena, estado != null ? estado.getDescripcion() : "Activo");
        this.usuario = usuario;
        this.rol = rol != null ? rol.getDescripcion() : "Administrador";
        this.nivelAcceso = this.rol;
        this.rutaImagen = rutaImagen;
    }

    /**
     * Constructor flexible para formularios que combinan nombre completo y campos de contacto.
     */
    public Administrador(int id, String nombre, String telefono, String email, String direccion,
                         String usuario, String clave, String rutaImagen, ERoles rol, EEstados estado) {
        super(id, nombre, (nombre != null && nombre.contains(" ")) ? nombre.substring(nombre.lastIndexOf(" ") + 1) : nombre,
              email, clave, estado != null ? estado.getDescripcion() : "Activo");
        this.usuario = usuario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rutaImagen = rutaImagen;
        this.rol = rol != null ? rol.getDescripcion() : "Administrador";
        this.nivelAcceso = this.rol;
    }

    // ==========================================
    // GETTERS Y SETTERS CON ALIAS PARA FXML / BD
    // ==========================================

    public String getNombres() {
        return getNombre();
    }

    public void setNombres(String nombres) {
        setNombre(nombres);
    }

    public String getApellidos() {
        return getApellido();
    }

    public void setApellidos(String apellidos) {
        setApellido(apellidos);
    }

    public String getCorreo() {
        return getEmail();
    }

    public void setCorreo(String correo) {
        setEmail(correo);
    }

    public String getClaveAcceso() {
        return getContrasena();
    }

    public void setClaveAcceso(String claveAcceso) {
        setContrasena(claveAcceso);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol != null ? rol : nivelAcceso;
    }

    public void setRol(String rol) {
        this.rol = rol;
        this.nivelAcceso = rol;
    }

    public ERoles getRolEnum() {
        return ERoles.fromString(getRol());
    }

    public void setRolEnum(ERoles rolEnum) {
        if (rolEnum != null) {
            setRol(rolEnum.getDescripcion());
        }
    }

    public EEstados getEstadoEnum() {
        return EEstados.fromString(getEstado());
    }

    public void setEstadoEnum(EEstados estadoEnum) {
        if (estadoEnum != null) {
            setEstado(estadoEnum.getDescripcion());
        }
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getEditado() {
        return editado;
    }

    public void setEditado(String editado) {
        this.editado = editado;
    }

    public String getNivelAcceso() {
        return nivelAcceso != null ? nivelAcceso : rol;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String> getBitacora() {
        return new ArrayList<>(bitacora);
    }

    public String getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(String estadoTurno) {
        if (estadoTurno == null || !ESTADOS_TURNO.contains(estadoTurno)) {
            throw new IllegalArgumentException("Estado turno invalido. Valores permitidos: " + ESTADOS_TURNO);
        }
        this.estadoTurno = estadoTurno;
    }
    
    public void setBitacora(List<String> bitacora) {
        if (bitacora == null) {
            throw new IllegalArgumentException("La bitacora no puede ser nula");
        }
        this.bitacora = new ArrayList<>(bitacora);
    }
    
    public void agregarEntrada(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }
        bitacora.add(entrada.trim());
    }

    @Override
    public String toString() {
        return super.toString() + " Administrador{" +
                "usuario='" + usuario + '\'' +
                ", rol='" + getRol() + '\'' +
                ", rutaImagen='" + rutaImagen + '\'' +
                ", creado='" + creado + '\'' +
                ", editado='" + editado + '\'' +
                ", nivelAcceso='" + nivelAcceso + '\'' +
                ", estadoTurno='" + estadoTurno + '\'' +
                '}';
    }
}
