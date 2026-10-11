package app.service.inputports;

import app.domain.ChangeLog;
import app.domain.Patient;
import app.domain.enums.DocumentTypeEnum;
import app.domain.enums.EpsEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Puerto de entrada: lo que la VIEW le puede pedir al service de pacientes.
 * Cada método sale de una historia de usuario (HU-01 y HU-05).
 */
public interface PatientServicePort {

    // HU-01 Registro de paciente
    Patient register(DocumentTypeEnum documentType, String documentNumber, String fullName, LocalDate birthDate,
                     String email, String phone, String address, EpsEnum eps,
                     String password, boolean dataPolicyAccepted);

    Patient findByDocument(String documentNumber);

    // HU-05 Edición de perfil y contacto
    void requestEmailChange(Patient patient, String newEmail);

    void confirmEmailChange(Patient patient, String code);

    void requestPhoneChange(Patient patient, String newPhone);

    void confirmPhoneChange(Patient patient, String code);

    void updateAddress(Patient patient, String newAddress);

    void updateEps(Patient patient, EpsEnum newEps);

    List<ChangeLog> getChangeHistory(Patient patient);
}
