package app.repository;

import app.domain.Patient;
import app.service.outputports.PatientRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida usando una lista en memoria.
 * Si mañana usan una base de datos, se crea otra clase que implemente
 * PatientRepositoryPort y el service NO cambia.
 */
public class PatientRepositoryAdapter implements PatientRepositoryPort {

    private final List<Patient> patients = new ArrayList<>();

    @Override
    public void save(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void update(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getDocumentNumber().equals(patient.getDocumentNumber())) {
                patients.set(i, patient);
                return;
            }
        }
    }

    @Override
    public Optional<Patient> findByDocument(String documentNumber) {
        return patients.stream()
                .filter(p -> p.getDocumentNumber().equalsIgnoreCase(documentNumber))
                .findFirst();
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByDocument(String documentNumber) {
        return findByDocument(documentNumber).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
