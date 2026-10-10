package app.configuration;

import app.repository.PatientRepositoryAdapter;
import app.repository.RecoveryTokenRepositoryAdapter;
import app.repository.SessionRepositoryAdapter;
import app.service.AuthServiceAdapter;
import app.service.PatientServiceAdapter;
import app.service.RecoveryServiceAdapter;
import app.service.inputports.AuthServicePort;
import app.service.inputports.PatientServicePort;
import app.service.inputports.RecoveryServicePort;
import app.service.outputports.PatientRepositoryPort;
import app.service.outputports.RecoveryTokenRepositoryPort;
import app.service.outputports.SessionRepositoryPort;
import app.ui.CliUserInterface;
import app.view.AuthView;
import app.view.ConsoleInput;
import app.view.PatientView;
import app.view.RecoveryView;

import java.util.Scanner;

/**
 * Paso 8: aquí se "arma" la app. Es el ÚNICO lugar donde se usa "new" con las
 * clases concretas; el resto del código solo conoce interfaces.
 * Se construye de abajo hacia arriba: repository -> service -> view -> ui.
 */
public class AppConfig {

    public CliUserInterface createUserInterface() {
        // 1. Repositorios (adaptadores de salida)
        PatientRepositoryPort patientRepository = new PatientRepositoryAdapter();
        SessionRepositoryPort sessionRepository = new SessionRepositoryAdapter();
        RecoveryTokenRepositoryPort tokenRepository = new RecoveryTokenRepositoryAdapter();

        // 2. Services (reciben los repositorios como interfaces)
        PatientServicePort patientService = new PatientServiceAdapter(patientRepository);
        AuthServicePort authService = new AuthServiceAdapter(patientRepository, sessionRepository);
        RecoveryServicePort recoveryService =
                new RecoveryServiceAdapter(patientRepository, tokenRepository, sessionRepository);

        // 3. Views (reciben los services como interfaces)
        ConsoleInput input = new ConsoleInput(new Scanner(System.in));
        PatientView patientView = new PatientView(patientService, input);
        AuthView authView = new AuthView(authService, input);
        RecoveryView recoveryView = new RecoveryView(recoveryService, input);

        // 4. Registrar las verticales en la interfaz de usuario
        return new CliUserInterface(patientView, authView, recoveryView, input);
    }
}
