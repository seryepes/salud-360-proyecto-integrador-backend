package app.service.helpers;

import java.security.SecureRandom;

/**
 * Genera códigos numéricos aleatorios (verificación de correo, recuperación, cambio de contacto).
 */
public final class CodeGeneratorHelper {

    private static final SecureRandom RANDOM = new SecureRandom();

    private CodeGeneratorHelper() {
    }

    public static String generateNumericCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}
