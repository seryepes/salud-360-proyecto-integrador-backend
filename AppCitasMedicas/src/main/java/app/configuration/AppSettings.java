package app.configuration;

/**
 * Valores "configurables" que piden las historias de usuario.
 * Si el profe dice "cambien a 5 intentos", solo se toca aquí.
 * (Los tiempos están cortos para poder probarlos en clase.)
 */
public final class AppSettings {

    // HU-01
    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 120;

    // HU-02
    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;
    public static final int LOCK_MINUTES = 2;
    public static final int SESSION_TIMEOUT_MINUTES = 3;

    // HU-03
    public static final int RECOVERY_CODE_MINUTES = 10;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final String PASSWORD_RULES =
            "mínimo " + PASSWORD_MIN_LENGTH + " caracteres, al menos una mayúscula y al menos un número";

    private AppSettings() {
    }
}
