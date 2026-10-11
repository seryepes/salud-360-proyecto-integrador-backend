package app.service.helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cifra la contraseña (SHA-256) para no guardarla en texto plano.
 */
public final class PasswordHashHelper {

    private PasswordHashHelper() {
    }

    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo cifrar la contraseña.", e);
        }
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        return rawPassword != null && hash(rawPassword).equals(hashedPassword);
    }
}
