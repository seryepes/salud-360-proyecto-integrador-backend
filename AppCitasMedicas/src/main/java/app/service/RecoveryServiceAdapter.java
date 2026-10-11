package app.service;

import app.configuration.AppSettings;
import app.domain.Patient;
import app.domain.RecoveryToken;
import app.domain.enums.AccountStateEnum;
import app.domain.enums.RecoveryChannelEnum;
import app.service.helpers.AccountStateHelper;
import app.service.helpers.CodeGeneratorHelper;
import app.service.helpers.NotificationHelper;
import app.service.helpers.PasswordHashHelper;
import app.service.inputports.RecoveryServicePort;
import app.service.outputports.PatientRepositoryPort;
import app.service.outputports.RecoveryTokenRepositoryPort;
import app.service.outputports.SessionRepositoryPort;
import app.service.validators.PasswordValidator;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Lógica de recuperación de contraseña (HU-03).
 */
public class RecoveryServiceAdapter implements RecoveryServicePort {

    private static final int CODE_LENGTH = 6;
    private static final String INVALID_CODE = "El código es inválido o ya expiró.";

    private final PatientRepositoryPort patientRepository;
    private final RecoveryTokenRepositoryPort tokenRepository;
    private final SessionRepositoryPort sessionRepository;

    public RecoveryServiceAdapter(PatientRepositoryPort patientRepository,
                                  RecoveryTokenRepositoryPort tokenRepository,
                                  SessionRepositoryPort sessionRepository) {
        this.patientRepository = patientRepository;
        this.tokenRepository = tokenRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void requestRecovery(String email, RecoveryChannelEnum channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Debe elegir un canal de recuperación.");
        }
        Optional<Patient> found = patientRepository.findByEmail(email == null ? "" : email.trim());
        if (found.isEmpty()) {
            return; // Por seguridad no se avisa si el correo existe o no
        }
        Patient patient = found.get();

        // Solo el último código sirve
        tokenRepository.invalidateAllByPatient(patient.getDocumentNumber());

        String code = CodeGeneratorHelper.generateNumericCode(CODE_LENGTH);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(AppSettings.RECOVERY_CODE_MINUTES);
        tokenRepository.save(new RecoveryToken(patient.getDocumentNumber(), code, channel, expiresAt, false));

        String message = "Tu código de recuperación es " + code + ". Vence en "
                + AppSettings.RECOVERY_CODE_MINUTES + " minutos y solo se puede usar una vez.";
        if (channel == RecoveryChannelEnum.EMAIL) {
            NotificationHelper.sendEmail(patient.getEmail(), "Recuperación de contraseña", message);
        } else {
            NotificationHelper.sendSms(patient.getPhone(), message);
        }
    }

    @Override
    public void resetPassword(String email, String code, String newPassword) {
        Patient patient = patientRepository.findByEmail(email == null ? "" : email.trim())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CODE));

        RecoveryToken token = tokenRepository.findByPatientAndCode(patient.getDocumentNumber(), code)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CODE));

        if (token.isUsed() || LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new IllegalArgumentException(INVALID_CODE);
        }

        PasswordValidator.validate(newPassword);

        patient.setPassword(PasswordHashHelper.hash(newPassword));
        token.setUsed(true);
        tokenRepository.update(token);

        if (patient.getAccountState() == AccountStateEnum.LOCKED) {
            AccountStateHelper.unlock(patient);
        } else {
            AccountStateHelper.resetFailedAttempts(patient);
        }
        patientRepository.update(patient);

        // Cerrar las demás sesiones y notificar el cambio
        sessionRepository.closeAllByPatient(patient.getDocumentNumber());
        NotificationHelper.sendEmail(patient.getEmail(), "Tu contraseña fue cambiada",
                "Si no fuiste tú, comunícate con soporte de inmediato.");
    }
}
