package wat.grupa.trzy.wielkieakcjeitransakcje.other.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserDataRepository userDataRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public CustomAuthenticationSuccessHandler(UserDataRepository userDataRepository, SessionRepository sessionRepository) {
        this.userDataRepository = userDataRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userEmail = authentication.getName();

        UserData user = userDataRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Nie można znaleźć zalogowanego użytkownika w bazie danych."));

        Sessions newDbSession = new Sessions();
        newDbSession.setSession(UUID.randomUUID().toString());
        newDbSession.setUserData(user);
        sessionRepository.save(newDbSession);

        request.getSession().setAttribute("db_session_id", newDbSession.getSession());

        response.sendRedirect("/userData");
    }
}