package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_UserProfile;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;
import java.nio.file.AccessDeniedException;
@Service
public class UserProfileService {
    private final SessionRepository sessionRepository;

    public UserProfileService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    @Transactional(readOnly = true)
    public DTO_UserProfile getUserProfileBySessionId(String sessionId) throws AccessDeniedException {
        UserData user = sessionRepository.findBySession(sessionId)
                .orElseThrow(() -> new AccessDeniedException("Nieprawidłowa lub wygasła sesja."))
                .getUserData();

        return new DTO_UserProfile(user);
    }
}
