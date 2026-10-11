package app.view;

import app.configuration.AppSettings;
import app.domain.enums.RecoveryChannelEnum;
import app.service.inputports.RecoveryServicePort;

/**
 * View de recuperación de contraseña (HU-03).
 */
public class RecoveryView {

    private final RecoveryServicePort recoveryService;
    private final ConsoleInput input;

    public RecoveryView(RecoveryServicePort recoveryService, ConsoleInput input) {
        this.recoveryService = recoveryService;
        this.input = input;
    }

    public void recoverPassword() {
        input.showTitle("RECUPERAR CONTRASEÑA");
        String email = input.readText("Correo registrado");
        RecoveryChannelEnum channel = input.readEnum("¿Por dónde quiere recibir el código?", RecoveryChannelEnum.values());

        try {
            recoveryService.requestRecovery(email, channel);
            System.out.println("Si el correo está registrado, recibirá un código por el canal elegido.");

            String code = input.readText("Código recibido");
            System.out.println("La nueva contraseña debe tener " + AppSettings.PASSWORD_RULES + ".");
            String newPassword = input.readText("Nueva contraseña");

            recoveryService.resetPassword(email, code, newPassword);
            System.out.println("Contraseña actualizada. Ya puede iniciar sesión.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("No se pudo recuperar la contraseña: " + e.getMessage());
        }
    }
}
