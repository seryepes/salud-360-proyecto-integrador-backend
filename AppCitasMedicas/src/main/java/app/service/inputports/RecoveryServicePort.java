package app.service.inputports;

import app.domain.enums.RecoveryChannelEnum;

/**
 * Puerto de entrada para recuperar la contraseña (HU-03).
 */
public interface RecoveryServicePort {

    void requestRecovery(String email, RecoveryChannelEnum channel);

    void resetPassword(String email, String code, String newPassword);
}
