package app.service;

import app.configuration.AppSettings;
import app.domain.Patient;
import app.domain.Session;
import app.domain.enums.AccountStateEnum;
import app.service.helpers.AccountStateHelper;
import app.service.helpers.PasswordHashHelper;
import app.service.inputports.AuthServicePort;
import app.service.outputports.PatientRepositoryPort;
import app.service.outputports.SessionRepositoryPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Lógica de inicio de sesión (HU-02).
 */
public class AuthServiceAdapter implements AuthServicePort {

    private static final String GENERIC_ERROR = "Correo o contraseña incorrectos.";

    private final PatientRepositoryPort patientRepository;
    private final SessionRepositoryPort sessionRepository;

    public AuthServiceAdapter(PatientRepositoryPort patientRepository, SessionRepositoryPort sessionRepository) {
        this.patientRepository = patientRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session login(String email, String password) {
        // Mensaje genérico: no revela si falló el correo o la contraseña
        Patient patient = patientRepository.findByEmail(email == null ? "" : email.trim())
                .orElseThrow(() -> new IllegalArgumentException(GENERIC_ERROR));

        AccountStateHelper.unlockIfLockExpired(patient);

        if (patient.getAccountState() == AccountStateEnum.LOCKED) {
            String until = patient.getLockedUntil().format(DateTimeFormatter.ofPattern("HH:mm"));
            throw new IllegalStateException("Cuenta bloqueada temporalmente. Intente de nuevo después de las " + until + ".");
        }

        if (!PasswordHashHelper.matches(password, patient.getPassword())) {
            boolean locked = AccountStateHelper.registerFailedAttempt(patient);
            patientRepository.update(patient);
            if (locked) {
                throw new IllegalStateException("Superó el número de intentos. Su cuenta quedó bloqueada por "
                        + AppSettings.LOCK_MINUTES + " minutos.");
            }
            throw new IllegalArgumentException(GENERIC_ERROR);
        }

        AccountStateHelper.resetFailedAttempts(patient);
        patientRepository.update(patient);

        LocalDateTime now = LocalDateTime.now();
        Session session = new Session(UUID.randomUUID().toString(), patient.getDocumentNumber(), now, now, true);
        sessionRepository.save(session);
        return session;
    }

    @Override
    public boolean isSessionActive(Session session) {
        Session stored = sessionRepository.findById(session.getId()).orElse(null);
        if (stored == null || !stored.isActive()) {
            return false;
        }
        LocalDateTime limit = stored.getLastActivity().plusMinutes(AppSettings.SESSION_TIMEOUT_MINUTES);
        if (LocalDateTime.now().isAfter(limit)) {
            stored.setActive(false);
            sessionRepository.update(stored);
            return false;
        }
        stored.setLastActivity(LocalDateTime.now());
        sessionRepository.update(stored);
        return true;
    }

    @Override
    public void logout(Session session) {
        session.setActive(false);
        sessionRepository.update(session);
    }

    @Override
    public Patient getLoggedPatient(Session session) {
        return patientRepository.findByDocument(session.getPatientDocument())
                .orElseThrow(() -> new IllegalStateException("No se encontró el paciente de la sesión."));
    }
}
