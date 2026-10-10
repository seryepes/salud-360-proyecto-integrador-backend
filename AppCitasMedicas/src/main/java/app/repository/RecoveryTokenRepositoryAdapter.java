package app.repository;

import app.domain.RecoveryToken;
import app.service.outputports.RecoveryTokenRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecoveryTokenRepositoryAdapter implements RecoveryTokenRepositoryPort {

    private final List<RecoveryToken> tokens = new ArrayList<>();

    @Override
    public void save(RecoveryToken token) {
        tokens.add(token);
    }

    @Override
    public void update(RecoveryToken token) {
        // Al ser una lista en memoria, el objeto ya está actualizado por referencia.
        // Se deja el método porque con una base de datos sí haría falta.
    }

    @Override
    public Optional<RecoveryToken> findByPatientAndCode(String patientDocument, String code) {
        return tokens.stream()
                .filter(t -> t.getPatientDocument().equals(patientDocument) && t.getCode().equals(code))
                .findFirst();
    }

    @Override
    public void invalidateAllByPatient(String patientDocument) {
        tokens.stream()
                .filter(t -> t.getPatientDocument().equals(patientDocument))
                .forEach(t -> t.setUsed(true));
    }
}
