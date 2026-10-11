package app.ui;

import app.domain.Patient;
import app.domain.Session;
import app.view.AuthView;
import app.view.ConsoleInput;
import app.view.PatientView;
import app.view.RecoveryView;

/**
 * Menús del programa (paso 9). Solo decide QUÉ view llamar según la opción elegida.
 */
public class CliUserInterface {

    private final PatientView patientView;
    private final AuthView authView;
    private final RecoveryView recoveryView;
    private final ConsoleInput input;

    public CliUserInterface(PatientView patientView, AuthView authView,
                            RecoveryView recoveryView, ConsoleInput input) {
        this.patientView = patientView;
        this.authView = authView;
        this.recoveryView = recoveryView;
        this.input = input;
    }

    public void start() {
        boolean running = true;
        while (running) {
            input.showTitle("APP DE CITAS MÉDICAS");
            System.out.println("1. Registrarme");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Olvidé mi contraseña");
            System.out.println("0. Salir");
            int option = input.readOption("Opción", 0, 3);

            switch (option) {
                case 1 -> patientView.registerPatient();
                case 2 -> {
                    Session session = authView.login();
                    if (session != null) {
                        patientMenu(session);
                    }
                }
                case 3 -> recoveryView.recoverPassword();
                default -> {
                    running = false;
                    System.out.println("Hasta pronto.");
                }
            }
        }
    }

    private void patientMenu(Session session) {
        while (true) {
            input.showTitle("MI CUENTA");
            System.out.println("1. Ver mi perfil");
            System.out.println("2. Editar perfil y contacto");
            System.out.println("3. Ver historial de cambios");
            System.out.println("0. Cerrar sesión");
            int option = input.readOption("Opción", 0, 3);

            if (option == 0) {
                authView.logout(session);
                return;
            }
            if (!authView.checkSession(session)) {
                return;
            }
            Patient patient = authView.getLoggedPatient(session);
            switch (option) {
                case 1 -> patientView.showProfile(patient);
                case 2 -> patientView.editProfile(patient);
                default -> patientView.showChangeHistory(patient);
            }
        }
    }
}
