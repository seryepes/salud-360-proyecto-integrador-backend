package app.domain.enums;

/**
 * Tipos de documento permitidos en el registro (HU-01).
 * Se usa enum porque son valores FIJOS: el usuario elige, no escribe.
 */
public enum DocumentTypeEnum {
    CC("Cédula de ciudadanía"),
    CE("Cédula de extranjería"),
    PA("Pasaporte"),
    PPT("Permiso por protección temporal");

    private final String description;

    DocumentTypeEnum(String description) {
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
