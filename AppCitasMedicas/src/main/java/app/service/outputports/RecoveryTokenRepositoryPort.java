package app.service.outputports;

import app.domain.RecoveryToken;

import java.util.Optional;

/**
 * Puerto de salida para los códigos de recuperación (HU-03).
 */
public interface RecoveryTokenRepositoryPort {

    void save(RecoveryToken token);

    void update(RecoveryToken token);

    Optional<RecoveryToken> findByPatientAndCode(String patientDocument, String code);

    void invalidateAllByPatient(String patientDocument);
}
