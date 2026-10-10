package app.domain;

import app.domain.enums.RecoveryChannelEnum;

import java.time.LocalDateTime;

/**
 * Código de recuperación de contraseña (HU-03): expira y es de un solo uso.
 */
public class RecoveryToken {

    private String patientDocument;
    private String code;
    private RecoveryChannelEnum channel;
    private LocalDateTime expiresAt;
    private boolean used;

    public RecoveryToken() {
    }

    public RecoveryToken(String patientDocument, String code, RecoveryChannelEnum channel,
                         LocalDateTime expiresAt, boolean used) {
        this.patientDocument = patientDocument;
        this.code = code;
        this.channel = channel;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    public String getPatientDocument() {
        return patientDocument;
    }

    public void setPatientDocument(String patientDocument) {
        this.patientDocument = patientDocument;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RecoveryChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(RecoveryChannelEnum channel) {
        this.channel = channel;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
