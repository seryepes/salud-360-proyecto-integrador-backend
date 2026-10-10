package app.service.validators;

import app.configuration.AppSettings;

/**
 * Reglas de seguridad de la contraseña (HU-03: longitud, mayúsculas, números).
 */
public final class PasswordValidator {

    private PasswordValidator() {
    }

    public static void validate(String password) {
        DataValidator.requireText(password, "contraseña");
        boolean longEnough = password.length() >= AppSettings.PASSWORD_MIN_LENGTH;
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        if (!longEnough || !hasUpper || !hasDigit) {
            throw new IllegalArgumentException("La contraseña no es segura. Debe tener " + AppSettings.PASSWORD_RULES + ".");
        }
    }
}
