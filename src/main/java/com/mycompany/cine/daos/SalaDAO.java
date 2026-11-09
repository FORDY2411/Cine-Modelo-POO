package com.mycompany.cine.daos;

import com.mycompany.cine.modelo.Sala;
import com.mycompany.cine.persistencia.Listable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Acceso a Datos (DAO) para la entidad Sala.
 * Implementa las operaciones CRUD sobre la tabla 'salas' de MySQL (cine_db)
 * y cumple con el contrato Listable<Sala>.
 */
public class SalaDAO implements Listable<Sala> {

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    /**
     * Inserta una nueva sala en la tabla 'salas'.
     *
     * @param sala objeto con los datos a persistir
     * @return true si la inserción fue exitosa, false en caso de error
     */
    public boolean guardar(Sala sala) {
        if (sala == null) return false;

        String sql = "INSERT INTO salas (numero, capacidad, tecnologia, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sala.getNumero());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getTecnologia() != null ? sala.getTecnologia() : "2D");
            ps.setString(4, sala.getEstadoSala() != null ? sala.getEstadoSala() : "Disponible");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sala.setIdSala(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.guardar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de una sala existente por su ID.
     *
     * @param sala objeto con los datos actualizados
     * @return true si se actualizó el registro, false en caso contrario
     */
    @Override
    public boolean actualizar(Sala sala) {
        if (sala == null || sala.getIdSala() <= 0) return false;

        String sql = "UPDATE salas SET numero = ?, capacidad = ?, tecnologia = ?, estado = ? WHERE id_sala = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sala.getNumero());
            ps.setInt(2, sala.getCapacidad());
            ps.setString(3, sala.getTecnologia());
            ps.setString(4, sala.getEstadoSala());
            ps.setInt(5, sala.getIdSala());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una sala por su ID numérico.
     *
     * @param id identificador de la sala
     * @return true si se eliminó, false en caso contrario
     */
    public boolean eliminar(int id) {
        if (id <= 0) return false;

        String sql = "DELETE FROM salas WHERE id_sala = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una sala por su ID.
     *
     * @param id identificador de la sala
     * @return objeto Sala o null si no se encuentra
     */
    public Sala obtenerPorId(int id) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM salas WHERE id_sala = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearSala(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve la lista completa de salas registradas en la base de datos.
     *
     * @return ArrayList de Sala
     */
    public ArrayList<Sala> listarTodos() {
        ArrayList<Sala> lista = new ArrayList<>();
        String sql = "SELECT * FROM salas ORDER BY numero ASC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearSala(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // ==========================================
    // IMPLEMENTACIÓN CONTRATO Listable<Sala>
    // ==========================================

    @Override
    public boolean agregar(Sala elemento) {
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
    public ArrayList<Sala> listar() {
        return listarTodos();
    }

    @Override
    public ArrayList<Sala> consultar(String dato) {
        ArrayList<Sala> resultados = new ArrayList<>();
        if (dato == null || dato.trim().isEmpty()) {
            return listarTodos();
        }

        String sql = "SELECT * FROM salas WHERE tecnologia LIKE ? OR estado LIKE ? ORDER BY numero ASC";
        String comodin = "%" + dato.trim() + "%";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, comodin);
            ps.setString(2, comodin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearSala(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en SalaDAO.consultar: " + e.getMessage());
        }
        return resultados;
    }

    @Override
    public Sala traer(String dato) {
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

    private Sala mapearSala(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_sala");
        int numero = rs.getInt("numero");
        int capacidad = rs.getInt("capacidad");
        String tecnologia = rs.getString("tecnologia");
        String estado = rs.getString("estado");

        return new Sala(id, numero, capacidad, tecnologia, estado);
    }
}
