package app.repository;

import app.domain.Session;
import app.service.outputports.SessionRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepositoryAdapter implements SessionRepositoryPort {

    private final List<Session> sessions = new ArrayList<>();

    @Override
    public void save(Session session) {
        sessions.add(session);
    }

    @Override
    public void update(Session session) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId().equals(session.getId())) {
                sessions.set(i, session);
                return;
            }
        }
    }

    @Override
    public Optional<Session> findById(String id) {
        return sessions.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    @Override
    public void closeAllByPatient(String patientDocument) {
        sessions.stream()
                .filter(s -> s.getPatientDocument().equals(patientDocument))
                .forEach(s -> s.setActive(false));
    }
}
