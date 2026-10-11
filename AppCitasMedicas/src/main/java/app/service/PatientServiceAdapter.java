package app.service;

import app.domain.ChangeLog;
import app.domain.Patient;
import app.domain.enums.DocumentTypeEnum;
import app.domain.enums.EpsEnum;
import app.service.helpers.CodeGeneratorHelper;
import app.service.helpers.NotificationHelper;
import app.service.helpers.PasswordHashHelper;
import app.service.inputports.PatientServicePort;
import app.service.outputports.PatientRepositoryPort;
import app.service.validators.DataValidator;
import app.service.validators.PasswordValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Lógica de negocio de pacientes (HU-01 y HU-05).
 * Recibe el repositorio por constructor como INTERFAZ (inyección de dependencias).
 */
public class PatientServiceAdapter implements PatientServicePort {

    private static final int CODE_LENGTH = 6;

    private final PatientRepositoryPort patientRepository;

    public PatientServiceAdapter(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ===================== HU-01 =====================

    @Override
    public Patient register(DocumentTypeEnum documentType, String documentNumber, String fullName, LocalDate birthDate,
                            String email, String phone, String address, EpsEnum eps,
                            String password, boolean dataPolicyAccepted) {
        // 1. Validar (criterios de aceptación)
        if (documentType == null) {
            throw new IllegalArgumentException("Debe seleccionar el tipo de documento.");
        }
        DataValidator.validateDocumentNumber(documentNumber);
        DataValidator.requireText(fullName, "nombre completo");
        DataValidator.validateAge(birthDate);
        DataValidator.validateEmail(email);
        DataValidator.validatePhone(phone);
        if (eps == null) {
            throw new IllegalArgumentException("Debe seleccionar la EPS o aseguradora.");
        }
        PasswordValidator.validate(password);
        if (!dataPolicyAccepted) {
            throw new IllegalArgumentException("Debe aceptar la política de tratamiento de datos personales para registrarse.");
        }
        if (patientRepository.existsByDocument(documentNumber.trim())) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con ese documento.");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (patientRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }

        // 2. El service crea el objeto de dominio (paso 6 del tablero)
        Patient patient = new Patient(documentType, documentNumber.trim(), fullName.trim(), birthDate,
                normalizedEmail, phone.trim(), address == null ? "" : address.trim(), eps,
                PasswordHashHelper.hash(password), true);

        // 3. Guardar: la cuenta queda activa de una vez
        patientRepository.save(patient);
        return patient;
    }

    @Override
    public Patient findByDocument(String documentNumber) {
        return patientRepository.findByDocument(documentNumber)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el paciente."));
    }

    // ===================== HU-05 =====================

    @Override
    public void requestEmailChange(Patient patient, String newEmail) {
        DataValidator.validateEmail(newEmail);
        String normalized = newEmail.trim().toLowerCase();
        if (normalized.equals(patient.getEmail())) {
            throw new IllegalArgumentException("El correo nuevo es igual al actual.");
        }
        if (patientRepository.existsByEmail(normalized)) {
            throw new IllegalArgumentException("Ese correo ya está registrado por otro paciente.");
        }
        String code = CodeGeneratorHelper.generateNumericCode(CODE_LENGTH);
        patient.setPendingEmail(normalized);
        patient.setVerificationCode(code);
        patientRepository.update(patient);
        NotificationHelper.sendEmail(normalized, "Confirma tu nuevo correo", "Tu código es: " + code);
    }

    @Override
    public void confirmEmailChange(Patient patient, String code) {
        if (patient.getPendingEmail() == null) {
            throw new IllegalStateException("No hay un cambio de correo pendiente.");
        }
        checkCode(patient, code);
        String oldEmail = patient.getEmail();
        patient.setEmail(patient.getPendingEmail());
        patient.setPendingEmail(null);
        patient.setVerificationCode(null);
        registerChange(patient, "Correo", oldEmail, patient.getEmail());
        patientRepository.update(patient);
    }

    @Override
    public void requestPhoneChange(Patient patient, String newPhone) {
        DataValidator.validatePhone(newPhone);
        String normalized = newPhone.trim();
        if (normalized.equals(patient.getPhone())) {
            throw new IllegalArgumentException("El teléfono nuevo es igual al actual.");
        }
        String code = CodeGeneratorHelper.generateNumericCode(CODE_LENGTH);
        patient.setPendingPhone(normalized);
        patient.setVerificationCode(code);
        patientRepository.update(patient);
        NotificationHelper.sendSms(normalized, "Tu código para confirmar el nuevo teléfono es: " + code);
    }

    @Override
    public void confirmPhoneChange(Patient patient, String code) {
        if (patient.getPendingPhone() == null) {
            throw new IllegalStateException("No hay un cambio de teléfono pendiente.");
        }
        checkCode(patient, code);
        String oldPhone = patient.getPhone();
        patient.setPhone(patient.getPendingPhone());
        patient.setPendingPhone(null);
        patient.setVerificationCode(null);
        registerChange(patient, "Teléfono", oldPhone, patient.getPhone());
        patientRepository.update(patient);
    }

    @Override
    public void updateAddress(Patient patient, String newAddress) {
        DataValidator.requireText(newAddress, "dirección");
        String oldAddress = patient.getAddress();
        patient.setAddress(newAddress.trim());
        registerChange(patient, "Dirección", oldAddress, patient.getAddress());
        patientRepository.update(patient);
    }

    @Override
    public void updateEps(Patient patient, EpsEnum newEps) {
        if (newEps == null) {
            throw new IllegalArgumentException("Debe seleccionar una EPS o aseguradora.");
        }
        if (newEps == patient.getEps()) {
            throw new IllegalArgumentException("Esa ya es su EPS actual.");
        }
        EpsEnum oldEps = patient.getEps();
        patient.setEps(newEps);
        registerChange(patient, "EPS", oldEps.getDescription(), newEps.getDescription());
        patientRepository.update(patient);
    }

    @Override
    public List<ChangeLog> getChangeHistory(Patient patient) {
        return Collections.unmodifiableList(patient.getChangeHistory());
    }

    // ===================== Métodos privados de apoyo =====================

    private void checkCode(Patient patient, String code) {
        if (patient.getVerificationCode() == null || !patient.getVerificationCode().equals(code)) {
            throw new IllegalArgumentException("El código no es correcto.");
        }
    }

    private void registerChange(Patient patient, String field, String oldValue, String newValue) {
        String user = patient.getFullName() + " (" + patient.getDocumentNumber() + ")";
        patient.getChangeHistory().add(new ChangeLog(LocalDateTime.now(), user, field, oldValue, newValue));
    }
}
