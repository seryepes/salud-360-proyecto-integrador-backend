package app.service.outputports;

import app.domain.Session;

import java.util.Optional;

/**
 * Puerto de salida para guardar las sesiones (HU-02 y HU-03).
 */
public interface SessionRepositoryPort {

    void save(Session session);

    void update(Session session);

    Optional<Session> findById(String id);

    void closeAllByPatient(String patientDocument);
}
