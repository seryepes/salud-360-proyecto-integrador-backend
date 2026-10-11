package app.domain.enums;

/**
 * EPS o aseguradoras disponibles (HU-01 y HU-05).
 */
public enum EpsEnum {
    SURA("EPS Sura"),
    SANITAS("Sanitas"),
    NUEVA_EPS("Nueva EPS"),
    SALUD_TOTAL("Salud Total"),
    COMPENSAR("Compensar"),
    FAMISANAR("Famisanar"),
    SAVIA_SALUD("Savia Salud"),
    PARTICULAR("Particular / aseguradora privada");

    private final String description;

    EpsEnum(String description) {
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
