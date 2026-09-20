package sv.edu.udb.emprendelink.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConexionBD {

    private static Map<String, String> variables;

    public static Connection obtenerConexion()
            throws SQLException {

        cargarVariablesSiHaceFalta();

        String url = variables.get("DB_URL");
        String usuario = variables.get("DB_USER");
        String contrasena = variables.get("DB_PASSWORD");

        if (url == null
                || usuario == null
                || contrasena == null) {

            throw new IllegalStateException(
                    "Faltan DB_URL, DB_USER o DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(
                url,
                usuario,
                contrasena
        );
    }

    private static synchronized void cargarVariablesSiHaceFalta() {

        if (variables != null) {
            return;
        }

        String ubicacion = System.getProperty(
                "emprendelink.env",
                ".env"
        );

        Path rutaEnv = Path.of(ubicacion);

        Map<String, String> valores = new HashMap<>();

        try {
            for (String linea : Files.readAllLines(rutaEnv)) {

                String limpia = linea.trim();

                if (limpia.isEmpty()
                        || limpia.startsWith("#")) {
                    continue;
                }

                int posicion = limpia.indexOf('=');

                if (posicion < 0) {
                    continue;
                }

                String clave = limpia.substring(
                        0,
                        posicion
                ).trim();

                String valor = limpia.substring(
                        posicion + 1
                ).trim();

                valores.put(clave, valor);
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo leer .env en: "
                            + rutaEnv.toAbsolutePath(),
                    e
            );
        }

        variables = valores;
    }
}