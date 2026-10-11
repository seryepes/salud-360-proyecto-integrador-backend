package app.service.outputports;

import app.domain.Patient;

import java.util.Optional;

/**
 * Puerto de salida: lo que el SERVICE necesita de la "bodega" de pacientes.
 * El service solo conoce esta interfaz; no sabe si detrás hay una lista, un archivo o una base de datos.
 */
public interface PatientRepositoryPort {

    void save(Patient patient);

    void update(Patient patient);

    Optional<Patient> findByDocument(String documentNumber);

    Optional<Patient> findByEmail(String email);

    boolean existsByDocument(String documentNumber);

    boolean existsByEmail(String email);
}
