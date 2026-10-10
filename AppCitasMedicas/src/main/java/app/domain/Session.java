package app.domain;

import java.time.LocalDateTime;

/**
 * Sesión iniciada por un paciente (HU-02). Guarda la última actividad
 * para poder cerrarla por inactividad.
 */
public class Session {

    private String id;
    private String patientDocument;
    private LocalDateTime startedAt;
    private LocalDateTime lastActivity;
    private boolean active;

    public Session() {
    }

    public Session(String id, String patientDocument, LocalDateTime startedAt,
                   LocalDateTime lastActivity, boolean active) {
        this.id = id;
        this.patientDocument = patientDocument;
        this.startedAt = startedAt;
        this.lastActivity = lastActivity;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientDocument() {
        return patientDocument;
    }

    public void setPatientDocument(String patientDocument) {
        this.patientDocument = patientDocument;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
