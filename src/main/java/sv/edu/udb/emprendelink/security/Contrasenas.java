package sv.edu.udb.emprendelink.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public final class Contrasenas {

    private static final int ITERACIONES = 210_000;
    private static final int LONGITUD_SAL = 16;
    private static final int LONGITUD_HASH = 256;

    private static final SecureRandom ALEATORIO = new SecureRandom();

    private Contrasenas() {
    }

    public static String generarHash(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }

        byte[] sal = new byte[LONGITUD_SAL];
        ALEATORIO.nextBytes(sal);

        byte[] hash = derivar(contrasena, sal, ITERACIONES);

        return "pbkdf2_sha256"
                + "$" + ITERACIONES
                + "$" + Base64.getEncoder().encodeToString(sal)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(
            String contrasena,
            String hashGuardado) {

        if (contrasena == null || hashGuardado == null) {
            return false;
        }

        try {
            String[] partes = hashGuardado.split("\\$");

            if (partes.length != 4
                    || !"pbkdf2_sha256".equals(partes[0])) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones < 100_000 || iteraciones > 1_000_000) {
                return false;
            }

            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            if (sal.length != LONGITUD_SAL
                    || hashOriginal.length != LONGITUD_HASH / 8) {
                return false;
            }

            byte[] hashCalculado =
                    derivar(contrasena, sal, iteraciones);

            return MessageDigest.isEqual(
                    hashOriginal,
                    hashCalculado
            );

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(
            String contrasena,
            byte[] sal,
            int iteraciones) {

        char[] caracteres = contrasena.toCharArray();

        PBEKeySpec especificacion = new PBEKeySpec(
                caracteres,
                sal,
                iteraciones,
                LONGITUD_HASH
        );

        try {
            SecretKeyFactory fabrica =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            return fabrica.generateSecret(
                    especificacion
            ).getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "No se pudo procesar la contraseña.",
                    e
            );

        } finally {
            especificacion.clearPassword();
            Arrays.fill(caracteres, '\0');
        }
    }
}