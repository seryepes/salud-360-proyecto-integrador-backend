package app.service.helpers;

import app.configuration.AppSettings;
import app.domain.Patient;
import app.domain.enums.AccountStateEnum;

import java.time.LocalDateTime;

/**
 * Cambia el estado de la cuenta del paciente (activar, bloquear, desbloquear).
 * Es como el SetUserState del proyecto del profe.
 */
public final class AccountStateHelper {

    private AccountStateHelper() {
    }

    /** Suma un intento fallido y bloquea si llegó al máximo. Devuelve true si quedó bloqueada. */
    public static boolean registerFailedAttempt(Patient patient) {
        patient.setFailedLoginAttempts(patient.getFailedLoginAttempts() + 1);
        if (patient.getFailedLoginAttempts() >= AppSettings.MAX_FAILED_LOGIN_ATTEMPTS) {
            patient.setAccountState(AccountStateEnum.LOCKED);
            patient.setLockedUntil(LocalDateTime.now().plusMinutes(AppSettings.LOCK_MINUTES));
            return true;
        }
        return false;
    }

    public static void unlockIfLockExpired(Patient patient) {
        if (patient.getAccountState() == AccountStateEnum.LOCKED
                && patient.getLockedUntil() != null
                && LocalDateTime.now().isAfter(patient.getLockedUntil())) {
            unlock(patient);
        }
    }

    public static void unlock(Patient patient) {
        patient.setAccountState(AccountStateEnum.ACTIVE);
        patient.setLockedUntil(null);
        patient.setFailedLoginAttempts(0);
    }

    public static void resetFailedAttempts(Patient patient) {
        patient.setFailedLoginAttempts(0);
    }
}
