package app.view;

import app.configuration.AppSettings;
import app.domain.ChangeLog;
import app.domain.Patient;
import app.domain.enums.DocumentTypeEnum;
import app.domain.enums.EpsEnum;
import app.service.inputports.PatientServicePort;

import java.time.LocalDate;
import java.util.List;

/**
 * View de pacientes: CAPTURA datos y MUESTRA resultados (HU-01 y HU-05).
 * No valida reglas de negocio ni crea objetos: eso lo hace el service.
 */
public class PatientView {

    private static final String DATA_POLICY =
            "Política de tratamiento de datos: sus datos personales y de salud se usarán solo para "
            + "gestionar sus citas médicas, conforme a la Ley 1581 de 2012.";

    private final PatientServicePort patientService;
    private final ConsoleInput input;

    public PatientView(PatientServicePort patientService, ConsoleInput input) {
        this.patientService = patientService;
        this.input = input;
    }

    // ===================== HU-01 =====================

    public void registerPatient() {
        input.showTitle("REGISTRO DE PACIENTE");
        DocumentTypeEnum documentType = input.readEnum("Tipo de documento", DocumentTypeEnum.values());
        String documentNumber = input.readText("Número de documento");
        String fullName = input.readText("Nombre completo");
        LocalDate birthDate = input.readDate("Fecha de nacimiento (AAAA-MM-DD)");
        String email = input.readText("Correo electrónico");
        String phone = input.readText("Celular (10 dígitos)");
        String address = input.readText("Dirección (opcional, Enter para omitir)");
        EpsEnum eps = input.readEnum("EPS o aseguradora", EpsEnum.values());
        System.out.println("La contraseña debe tener " + AppSettings.PASSWORD_RULES + ".");
        String password = input.readText("Contraseña");
        System.out.println(DATA_POLICY);
        boolean accepted = input.readYesNo("¿Acepta la política de tratamiento de datos? (s/n)");

        try {
            Patient patient = patientService.register(documentType, documentNumber, fullName, birthDate,
                    email, phone, address, eps, password, accepted);
            System.out.println("Registro exitoso, " + patient.getFullName() + ". Ya puede iniciar sesión.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    public void showProfile(Patient patient) {
        input.showTitle("MI PERFIL");
        System.out.println("Documento:      " + patient.getDocumentType() + " " + patient.getDocumentNumber());
        System.out.println("Nombre:         " + patient.getFullName());
        System.out.println("Nacimiento:     " + patient.getBirthDate());
        System.out.println("Correo:         " + patient.getEmail());
        System.out.println("Teléfono:       " + patient.getPhone());
        System.out.println("Dirección:      " + (patient.getAddress().isBlank() ? "(sin registrar)" : patient.getAddress()));
        System.out.println("EPS:            " + patient.getEps());
        System.out.println("Estado cuenta:  " + patient.getAccountState());
    }

    // ===================== HU-05 =====================

    public void editProfile(Patient patient) {
        input.showTitle("EDITAR PERFIL");
        System.out.println("1. Correo");
        System.out.println("2. Teléfono");
        System.out.println("3. Dirección");
        System.out.println("4. EPS / aseguradora");
        System.out.println("5. Documento o fecha de nacimiento");
        System.out.println("0. Volver");
        int option = input.readOption("Opción", 0, 5);

        try {
            switch (option) {
                case 1 -> changeEmail(patient);
                case 2 -> changePhone(patient);
                case 3 -> {
                    patientService.updateAddress(patient, input.readText("Nueva dirección"));
                    System.out.println("Dirección actualizada.");
                }
                case 4 -> {
                    patientService.updateEps(patient, input.readEnum("Nueva EPS o aseguradora", EpsEnum.values()));
                    System.out.println("EPS actualizada.");
                }
                case 5 -> System.out.println("El documento y la fecha de nacimiento no se pueden editar directamente. "
                        + "Debe enviar una solicitud de corrección con soporte del documento.");
                default -> {
                    // 0: volver
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("No se pudo actualizar: " + e.getMessage());
        }
    }

    private void changeEmail(Patient patient) {
        patientService.requestEmailChange(patient, input.readText("Nuevo correo"));
        String code = input.readText("Ingrese el código enviado al nuevo correo");
        patientService.confirmEmailChange(patient, code);
        System.out.println("Correo actualizado.");
    }

    private void changePhone(Patient patient) {
        patientService.requestPhoneChange(patient, input.readText("Nuevo celular (10 dígitos)"));
        String code = input.readText("Ingrese el código enviado por SMS");
        patientService.confirmPhoneChange(patient, code);
        System.out.println("Teléfono actualizado.");
    }

    public void showChangeHistory(Patient patient) {
        input.showTitle("HISTORIAL DE CAMBIOS");
        List<ChangeLog> history = patientService.getChangeHistory(patient);
        if (history.isEmpty()) {
            System.out.println("No hay cambios registrados.");
            return;
        }
        history.forEach(System.out::println);
    }
}
