package app.service.validators;

import app.configuration.AppSettings;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

/**
 * Valida formatos de datos (HU-01 y HU-05). Responde una sola pregunta: "¿este dato es válido?".
 * Si no lo es, lanza una excepción con un mensaje claro para el usuario.
 */
public final class DataValidator {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^3\\d{9}$");
    private static final Pattern DOCUMENT = Pattern.compile("^[A-Za-z0-9]{5,15}$");

    private DataValidator() {
    }

    public static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' es obligatorio.");
        }
    }

    public static void validateDocumentNumber(String documentNumber) {
        requireText(documentNumber, "número de documento");
        if (!DOCUMENT.matcher(documentNumber.trim()).matches()) {
            throw new IllegalArgumentException("El documento debe tener entre 5 y 15 letras o números, sin espacios ni puntos.");
        }
    }

    public static void validateEmail(String email) {
        requireText(email, "correo");
        if (!EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido (ejemplo: nombre@correo.com).");
        }
    }

    public static void validatePhone(String phone) {
        requireText(phone, "teléfono");
        if (!PHONE.matcher(phone.trim()).matches()) {
            throw new IllegalArgumentException("El teléfono debe ser un celular de 10 dígitos que empiece por 3.");
        }
    }

    public static void validateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < AppSettings.MIN_AGE || age > AppSettings.MAX_AGE) {
            throw new IllegalArgumentException("Edad no válida: el paciente debe tener entre "
                    + AppSettings.MIN_AGE + " y " + AppSettings.MAX_AGE + " años.");
        }
    }
}
