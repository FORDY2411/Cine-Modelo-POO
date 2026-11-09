package com.mycompany.cine.daos;

import com.mycompany.cine.modelo.Pelicula;
import com.mycompany.cine.persistencia.Listable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Acceso a Datos (DAO) para la entidad Pelicula.
 * Implementa las operaciones CRUD directamente sobre la tabla 'peliculas' de MySQL (cine_db)
 * y cumple con el contrato Listable<Pelicula>.
 */
public class PeliculaDAO implements Listable<Pelicula> {

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    /**
     * Inserta una nueva película en la tabla 'peliculas'.
     *
     * @param pelicula objeto con los datos a persistir
     * @return true si la inserción fue exitosa, false en caso de error
     */
    public boolean guardar(Pelicula pelicula) {
        if (pelicula == null) return false;

        String sql = "INSERT INTO peliculas (titulo, duracion_min, clasificacion, genero, estado, ruta_imagen) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setInt(2, pelicula.getDuracion());
            ps.setString(3, pelicula.getClasificacion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getEstadoPelicula() != null ? pelicula.getEstadoPelicula() : "En Cartelera");
            ps.setString(6, pelicula.getRutaImagen() != null ? pelicula.getRutaImagen() : "default_movie.png");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pelicula.setIdPelicula(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.guardar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de una película existente por su ID.
     *
     * @param pelicula objeto con los datos actualizados
     * @return true si se actualizó el registro, false en caso contrario
     */
    @Override
    public boolean actualizar(Pelicula pelicula) {
        if (pelicula == null || pelicula.getIdPelicula() <= 0) return false;

        String sql = "UPDATE peliculas SET titulo = ?, duracion_min = ?, clasificacion = ?, genero = ?, " +
                     "estado = ?, ruta_imagen = ? WHERE id_pelicula = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setInt(2, pelicula.getDuracion());
            ps.setString(3, pelicula.getClasificacion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getEstadoPelicula());
            ps.setString(6, pelicula.getRutaImagen());
            ps.setInt(7, pelicula.getIdPelicula());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una película por su ID numérico.
     *
     * @param id identificador de la película
     * @return true si se eliminó, false en caso contrario
     */
    public boolean eliminar(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM peliculas WHERE id_pelicula = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una película por su ID.
     *
     * @param id identificador de la película
     * @return objeto Pelicula o null si no se encuentra
     */
    public Pelicula obtenerPorId(int id) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM peliculas WHERE id_pelicula = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPelicula(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve la lista completa de películas registradas en la base de datos.
     *
     * @return ArrayList de Pelicula
     */
    public ArrayList<Pelicula> listarTodos() {
        ArrayList<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas ORDER BY id_pelicula ASC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPelicula(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // ==========================================
    // IMPLEMENTACIÓN CONTRATO Listable<Pelicula>
    // ==========================================

    @Override
    public boolean agregar(Pelicula elemento) {
        return guardar(elemento);
    }

    @Override
    public boolean eliminar(String dato) {
        if (dato == null || dato.trim().isEmpty()) return false;
        try {
            int id = Integer.parseInt(dato.trim());
            return eliminar(id);
        } catch (NumberFormatException e) {
            String sql = "DELETE FROM peliculas WHERE titulo = ?";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dato.trim());
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                System.err.println("Error al eliminar pelicula por título: " + ex.getMessage());
                return false;
            }
        }
    }

    @Override
    public ArrayList<Pelicula> listar() {
        return listarTodos();
    }

    @Override
    public ArrayList<Pelicula> consultar(String dato) {
        ArrayList<Pelicula> resultados = new ArrayList<>();
        if (dato == null || dato.trim().isEmpty()) {
            return listarTodos();
        }

        String sql = "SELECT * FROM peliculas WHERE titulo LIKE ? OR genero LIKE ? OR clasificacion LIKE ? ORDER BY id_pelicula ASC";
        String comodin = "%" + dato.trim() + "%";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, comodin);
            ps.setString(2, comodin);
            ps.setString(3, comodin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearPelicula(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en PeliculaDAO.consultar: " + e.getMessage());
        }
        return resultados;
    }

    @Override
    public Pelicula traer(String dato) {
        if (dato == null || dato.trim().isEmpty()) return null;

        try {
            int id = Integer.parseInt(dato.trim());
            return obtenerPorId(id);
        } catch (NumberFormatException e) {
            String sql = "SELECT * FROM peliculas WHERE titulo = ? LIMIT 1";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dato.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearPelicula(rs);
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error en PeliculaDAO.traer por título: " + ex.getMessage());
            }
        }
        return null;
    }

    // ==========================================
    // HELPER DE MAPEO
    // ==========================================

    private Pelicula mapearPelicula(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_pelicula");
        String titulo = rs.getString("titulo");
        int duracion = rs.getInt("duracion_min");
        String clasificacion = rs.getString("clasificacion");
        String genero = rs.getString("genero");
        String estado = rs.getString("estado");
        String rutaImagen = rs.getString("ruta_imagen");

        return new Pelicula(id, titulo, duracion, clasificacion, genero, estado, rutaImagen);
    }
}
