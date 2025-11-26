package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;

@Service
public class ApiLogoutService {

    private final SessionRepository sessionRepository;

    public ApiLogoutService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void logout(String sessionId) {
        sessionRepository.deleteBySession(sessionId);
    }
}