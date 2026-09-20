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

    public static Connection obtenerConexion() throws SQLException {
        cargarVariablesSiHaceFalta();

        String url = variables.get("DB_URL");
        String usuario = variables.get("DB_USER");
        String contrasena = variables.get("DB_PASSWORD");

        if (url == null || usuario == null || contrasena == null) {
            throw new IllegalStateException(
                    "Faltan variables DB_URL, DB_USER o DB_PASSWORD en el archivo .env");
        }

        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private static void cargarVariablesSiHaceFalta() {
        if (variables != null) {
            return;
        }

        variables = new HashMap<>();
        Path rutaEnv = Path.of(".env");

        try {
            for (String linea : Files.readAllLines(rutaEnv)) {
                String limpia = linea.trim();

                if (limpia.isEmpty() || limpia.startsWith("#")) {
                    continue;
                }

                int posIgual = limpia.indexOf('=');
                if (posIgual == -1) {
                    continue;
                }

                String clave = limpia.substring(0, posIgual).trim();
                String valor = limpia.substring(posIgual + 1).trim();
                variables.put(clave, valor);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo leer el archivo .env. ¿Existe en la raíz del proyecto?", e);
        }
    }
}
