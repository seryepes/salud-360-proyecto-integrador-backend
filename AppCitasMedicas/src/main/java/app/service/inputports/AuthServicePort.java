package app.service.inputports;

import app.domain.Patient;
import app.domain.Session;

/**
 * Puerto de entrada para el inicio de sesión (HU-02).
 */
public interface AuthServicePort {

    Session login(String email, String password);

    boolean isSessionActive(Session session);

    void logout(Session session);

    Patient getLoggedPatient(Session session);
}
