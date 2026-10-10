package app.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de un cambio en el perfil (HU-05: "se registra la fecha y el usuario de cada cambio").
 */
public class ChangeLog {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime changedAt;
    private String changedBy;
    private String field;
    private String oldValue;
    private String newValue;

    public ChangeLog() {
    }

    public ChangeLog(LocalDateTime changedAt, String changedBy, String field, String oldValue, String newValue) {
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return changedAt.format(FORMAT) + " | " + field + ": '" + oldValue + "' -> '" + newValue
                + "' | por " + changedBy;
    }
}
