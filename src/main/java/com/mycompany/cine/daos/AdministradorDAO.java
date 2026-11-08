package com.mycompany.cine.daos;

import com.mycompany.cine.modelo.Administrador;
import com.mycompany.cine.persistencia.Listable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Acceso a Datos (DAO) para la entidad Administrador.
 * Implementa las operaciones CRUD directamente sobre la tabla 'administradores' de MySQL/MariaDB
 * y cumple con el contrato Listable<Administrador> del proyecto Cine.
 */
public class AdministradorDAO implements Listable<Administrador> {

    // ==========================================
    // MÉTODOS DIRECTOS REQUERIDOS POR LA GUÍA
    // ==========================================

    /**
     * Inserta un nuevo registro de Administrador en la base de datos.
     *
     * @param admin objeto con los datos a persistir
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean guardar(Administrador admin) {
        if (admin == null) return false;

        String sql = "INSERT INTO administradores " +
                "(usuario, nombres, apellidos, correo, contrasena, rol, ruta_imagen, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getUsuario());
            ps.setString(2, admin.getNombres() != null ? admin.getNombres() : admin.getNombre());
            ps.setString(3, admin.getApellidos() != null ? admin.getApellidos() : admin.getApellido());
            ps.setString(4, admin.getCorreo() != null ? admin.getCorreo() : admin.getEmail());
            ps.setString(5, admin.getContrasena() != null ? admin.getContrasena() : "");
            ps.setString(6, admin.getRol() != null ? admin.getRol() : "Administrador");
            ps.setString(7, admin.getRutaImagen() != null ? admin.getRutaImagen() : "default_user.png");
            ps.setString(8, admin.getEstado() != null ? admin.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.guardar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la información de un Administrador existente por su ID.
     *
     * @param admin objeto con los datos actualizados
     * @return true si se actualizó al menos una fila, false en caso de error
     */
    @Override
    public boolean actualizar(Administrador admin) {
        if (admin == null || admin.getId() <= 0) return false;

        String sql = "UPDATE administradores SET " +
                "usuario = ?, nombres = ?, apellidos = ?, correo = ?, contrasena = ?, " +
                "rol = ?, ruta_imagen = ?, estado = ? " +
                "WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getUsuario());
            ps.setString(2, admin.getNombres() != null ? admin.getNombres() : admin.getNombre());
            ps.setString(3, admin.getApellidos() != null ? admin.getApellidos() : admin.getApellido());
            ps.setString(4, admin.getCorreo() != null ? admin.getCorreo() : admin.getEmail());
            ps.setString(5, admin.getContrasena() != null ? admin.getContrasena() : "");
            ps.setString(6, admin.getRol() != null ? admin.getRol() : "Administrador");
            ps.setString(7, admin.getRutaImagen() != null ? admin.getRutaImagen() : "default_user.png");
            ps.setString(8, admin.getEstado() != null ? admin.getEstado() : "Activo");
            ps.setInt(9, admin.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina físicamente un registro de la tabla administradores por su ID.
     *
     * @param id identificador de la fila a eliminar
     * @return true si la eliminación tuvo éxito, false si ocurrió un error
     */
    public boolean eliminar(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM administradores WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista completa de administradores ordenados de manera descendente por ID.
     *
     * @return {@link ArrayList} de administradores encontrados
     */
    public ArrayList<Administrador> listarTodos() {
        ArrayList<Administrador> lista = new ArrayList<>();
        String sql = "SELECT id, usuario, nombres, apellidos, correo, contrasena, rol, " +
                "ruta_imagen, estado, creado, editado FROM administradores ORDER BY id DESC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Administrador admin = mapearResultSet(rs);
                lista.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.listarTodos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca un administrador por su clave primaria ID.
     *
     * @param id identificador
     * @return instancia de {@link Administrador} o null si no se encuentra
     */
    public Administrador obtenerPorId(int id) {
        String sql = "SELECT id, usuario, nombres, apellidos, correo, contrasena, rol, " +
                "ruta_imagen, estado, creado, editado FROM administradores WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    // ==========================================
    // IMPLEMENTACIÓN CONTRATO Listable<Administrador>
    // ==========================================

    @Override
    public boolean agregar(Administrador elemento) {
        return guardar(elemento);
    }

    @Override
    public boolean eliminar(String dato) {
        if (dato == null || dato.trim().isEmpty()) return false;
        try {
            int id = Integer.parseInt(dato.trim());
            return eliminar(id);
        } catch (NumberFormatException e) {
            // Si el dato no es numérico, intentamos eliminar por usuario
            String sql = "DELETE FROM administradores WHERE usuario = ?";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dato.trim());
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                System.err.println("Error al eliminar por usuario: " + ex.getMessage());
                return false;
            }
        }
    }

    @Override
    public ArrayList<Administrador> listar() {
        return listarTodos();
    }

    @Override
    public ArrayList<Administrador> consultar(String dato) {
        ArrayList<Administrador> resultados = new ArrayList<>();
        if (dato == null || dato.trim().isEmpty()) {
            return listarTodos();
        }

        String sql = "SELECT id, usuario, nombres, apellidos, correo, contrasena, rol, " +
                "ruta_imagen, estado, creado, editado FROM administradores " +
                "WHERE nombres LIKE ? OR apellidos LIKE ? OR usuario LIKE ? OR correo LIKE ? " +
                "ORDER BY id DESC";

        String comodin = "%" + dato.trim() + "%";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, comodin);
            ps.setString(2, comodin);
            ps.setString(3, comodin);
            ps.setString(4, comodin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en AdministradorDAO.consultar: " + e.getMessage());
        }

        return resultados;
    }

    @Override
    public Administrador traer(String dato) {
        if (dato == null || dato.trim().isEmpty()) return null;

        try {
            int id = Integer.parseInt(dato.trim());
            return obtenerPorId(id);
        } catch (NumberFormatException e) {
            String sql = "SELECT id, usuario, nombres, apellidos, correo, contrasena, rol, " +
                    "ruta_imagen, estado, creado, editado FROM administradores WHERE usuario = ? LIMIT 1";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, dato.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearResultSet(rs);
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error en AdministradorDAO.traer: " + ex.getMessage());
            }
        }
        return null;
    }

    // ==========================================
    // MÉTODO AUXILIAR DE MAPEO RESULTSET -> OBJETO
    // ==========================================

    private Administrador mapearResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String usuario = rs.getString("usuario");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        String correo = rs.getString("correo");
        String contrasena = rs.getString("contrasena");
        String rol = rs.getString("rol");
        String rutaImagen = rs.getString("ruta_imagen");
        String estado = rs.getString("estado");
        String creado = rs.getString("creado");
        String editado = rs.getString("editado");

        Administrador admin = new Administrador(
                id, usuario, nombres, apellidos, correo, contrasena,
                rol, rutaImagen, estado, creado, editado
        );
        return admin;
    }
}
