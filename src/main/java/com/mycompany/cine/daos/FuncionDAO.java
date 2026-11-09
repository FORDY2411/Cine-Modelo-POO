package com.mycompany.cine.daos;

import com.mycompany.cine.modelo.Funcion;
import com.mycompany.cine.modelo.Pelicula;
import com.mycompany.cine.modelo.Sala;
import com.mycompany.cine.persistencia.Listable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Acceso a Datos (DAO) para la entidad Funcion.
 * Implementa las operaciones CRUD sobre la tabla 'funciones' de MySQL (cine_db)
 * asociando las entidades Pelicula y Sala, y cumple con el contrato Listable<Funcion>.
 */
public class FuncionDAO implements Listable<Funcion> {

    private final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private final SalaDAO salaDAO = new SalaDAO();

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    /**
     * Inserta una nueva función en la tabla 'funciones'.
     *
     * @param funcion objeto con los datos a persistir
     * @return true si la inserción fue exitosa, false en caso de error
     */
    public boolean guardar(Funcion funcion) {
        if (funcion == null || funcion.getPelicula() == null || funcion.getSala() == null) return false;

        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio_base, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, funcion.getPelicula().getIdPelicula());
            ps.setInt(2, funcion.getSala().getIdSala());
            ps.setTimestamp(3, Timestamp.valueOf(funcion.getFechaHora()));
            ps.setBigDecimal(4, funcion.getPrecioBase() != null ? funcion.getPrecioBase() : BigDecimal.ZERO);
            ps.setString(5, funcion.getEstadoFuncion() != null ? funcion.getEstadoFuncion() : "Programada");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        funcion.setIdFuncion(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error en FuncionDAO.guardar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de una función existente por su ID.
     *
     * @param funcion objeto con los datos actualizados
     * @return true si se actualizó el registro, false en caso contrario
     */
    @Override
    public boolean actualizar(Funcion funcion) {
        if (funcion == null || funcion.getIdFuncion() <= 0 || funcion.getPelicula() == null || funcion.getSala() == null) {
            return false;
        }

        String sql = "UPDATE funciones SET id_pelicula = ?, id_sala = ?, fecha_hora = ?, " +
                     "precio_base = ?, estado = ? WHERE id_funcion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, funcion.getPelicula().getIdPelicula());
            ps.setInt(2, funcion.getSala().getIdSala());
            ps.setTimestamp(3, Timestamp.valueOf(funcion.getFechaHora()));
            ps.setBigDecimal(4, funcion.getPrecioBase());
            ps.setString(5, funcion.getEstadoFuncion());
            ps.setInt(6, funcion.getIdFuncion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en FuncionDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una función por su ID numérico.
     *
     * @param id identificador de la función
     * @return true si se eliminó, false en caso contrario
     */
    public boolean eliminar(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM funciones WHERE id_funcion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en FuncionDAO.eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una función por su ID.
     *
     * @param id identificador de la función
     * @return objeto Funcion o null si no se encuentra
     */
    public Funcion obtenerPorId(int id) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM funciones WHERE id_funcion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en FuncionDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve la lista completa de funciones registradas en la base de datos con sus películas y salas asociadas.
     *
     * @return ArrayList de Funcion
     */
    public ArrayList<Funcion> listarTodos() {
        ArrayList<Funcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM funciones ORDER BY fecha_hora ASC, id_funcion ASC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Funcion f = mapearFuncion(rs);
                if (f != null) {
                    lista.add(f);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en FuncionDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // ==========================================
    // IMPLEMENTACIÓN CONTRATO Listable<Funcion>
    // ==========================================

    @Override
    public boolean agregar(Funcion elemento) {
        return guardar(elemento);
    }

    @Override
    public boolean eliminar(String dato) {
        if (dato == null || dato.trim().isEmpty()) return false;
        try {
            int id = Integer.parseInt(dato.trim());
            return eliminar(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public ArrayList<Funcion> listar() {
        return listarTodos();
    }

    @Override
    public ArrayList<Funcion> consultar(String dato) {
        ArrayList<Funcion> resultados = new ArrayList<>();
        if (dato == null || dato.trim().isEmpty()) {
            return listarTodos();
        }

        for (Funcion f : listarTodos()) {
            if ((f.getPelicula() != null && f.getPelicula().getTitulo().toLowerCase().contains(dato.toLowerCase().trim()))
                    || (f.getEstadoFuncion() != null && f.getEstadoFuncion().toLowerCase().contains(dato.toLowerCase().trim()))) {
                resultados.add(f);
            }
        }
        return resultados;
    }

    @Override
    public Funcion traer(String dato) {
        if (dato == null || dato.trim().isEmpty()) return null;

        try {
            int id = Integer.parseInt(dato.trim());
            return obtenerPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ==========================================
    // HELPER DE MAPEO
    // ==========================================

    private Funcion mapearFuncion(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_funcion");
        int idPelicula = rs.getInt("id_pelicula");
        int idSala = rs.getInt("id_sala");
        Timestamp ts = rs.getTimestamp("fecha_hora");
        LocalDateTime fechaHora = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
        BigDecimal precioBase = rs.getBigDecimal("precio_base");
        String estado = rs.getString("estado");

        Pelicula pelicula = peliculaDAO.obtenerPorId(idPelicula);
        Sala sala = salaDAO.obtenerPorId(idSala);

        // Fallbacks si la sala o película fue borrada o modificada externamente
        if (pelicula == null) {
            pelicula = new Pelicula(idPelicula, "Película #" + idPelicula, 120, "B", "General", "En Cartelera");
        }
        if (sala == null) {
            sala = new Sala(idSala, idSala, 50, "2D", "Disponible");
        }

        return new Funcion(id, pelicula, sala, fechaHora, precioBase, estado);
    }
}
