package app.domain.enums;

/**
 * Estados por los que pasa la cuenta de un paciente.
 * ACTIVE -> (muchos intentos fallidos) -> LOCKED -> (pasa el tiempo) -> ACTIVE
 */
public enum AccountStateEnum {
    ACTIVE("Activa"),
    LOCKED("Bloqueada temporalmente");

    private final String description;

    AccountStateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
