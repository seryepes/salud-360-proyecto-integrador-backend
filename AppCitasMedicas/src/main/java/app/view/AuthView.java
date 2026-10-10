package app.view;

import app.configuration.AppSettings;
import app.domain.Patient;
import app.domain.Session;
import app.service.inputports.AuthServicePort;

/**
 * View de inicio de sesión (HU-02).
 */
public class AuthView {

    private final AuthServicePort authService;
    private final ConsoleInput input;

    public AuthView(AuthServicePort authService, ConsoleInput input) {
        this.authService = authService;
        this.input = input;
    }

    /** Devuelve la sesión si el login fue exitoso, o null si falló. */
    public Session login() {
        input.showTitle("INICIAR SESIÓN");
        String email = input.readText("Correo");
        String password = input.readText("Contraseña");
        try {
            Session session = authService.login(email, password);
            Patient patient = authService.getLoggedPatient(session);
            System.out.println("Bienvenido(a), " + patient.getFullName() + ".");
            return session;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean checkSession(Session session) {
        if (authService.isSessionActive(session)) {
            return true;
        }
        System.out.println("Su sesión se cerró (inactividad de más de " + AppSettings.SESSION_TIMEOUT_MINUTES
                + " minutos o cambio de contraseña). Inicie sesión de nuevo.");
        return false;
    }

    public void logout(Session session) {
        authService.logout(session);
        System.out.println("Sesión cerrada.");
    }

    public Patient getLoggedPatient(Session session) {
        return authService.getLoggedPatient(session);
    }
}
