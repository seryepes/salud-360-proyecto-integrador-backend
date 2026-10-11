package app.domain.enums;

/**
 * Canales para recuperar la contraseña (HU-03).
 */
public enum RecoveryChannelEnum {
    EMAIL("Correo electrónico"),
    SMS("Mensaje de texto (SMS)");

    private final String description;

    RecoveryChannelEnum(String description) {
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
