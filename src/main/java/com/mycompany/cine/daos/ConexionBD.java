package com.mycompany.cine.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor centralizado de conexión JDBC con la base de datos MySQL / MariaDB (cine_db).
 */
public class ConexionBD {

    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BD = "cine_db";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BD
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    // Carga estática del driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Advertencia: Driver MySQL no encontrado en el classpath: " + e.getMessage());
        }
    }

    private ConexionBD() {
        // Constructor privado para clase utilitaria
    }

    /**
     * Obtiene una nueva conexión a la base de datos MySQL.
     *
     * @return objeto {@link Connection} listo para ejecutar consultas
     * @throws SQLException si la conexión no se puede establecer
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    /**
     * Comprueba si la base de datos está accesible.
     *
     * @return true si la conexión tuvo éxito, false en caso contrario
     */
    public static boolean probarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Fallo al probar conexión con la base de datos: " + e.getMessage());
            return false;
        }
    }
}
