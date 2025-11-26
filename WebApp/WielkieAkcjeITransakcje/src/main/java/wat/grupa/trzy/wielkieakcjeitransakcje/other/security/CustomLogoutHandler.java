package wat.grupa.trzy.wielkieakcjeitransakcje.other.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final SessionRepository sessionRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Autowired
    public CustomLogoutHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Object dbSessionIdObject = request.getSession().getAttribute("db_session_id");

        if (dbSessionIdObject instanceof String dbSessionId) {

            log.info("Znaleziono ID sesji z bazy danych: " + dbSessionId + ". Próba usunięcia...");
            sessionRepository.deleteBySession(dbSessionId);
            log.info("Pomyślnie wykonano polecenie usunięcia sesji.");
        } else {
            log.error("!!! Nie znaleziono atrybutu 'db_session_id' w sesji HTTP. Nie można usunąć sesji z bazy. !!!");
        }
    }
}