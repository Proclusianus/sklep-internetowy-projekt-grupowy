package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginRequest;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginResponse;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import javax.security.auth.login.LoginException;
import java.util.UUID;

@Service
public class LoginService {

    private final UserDataRepository userDataRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserDataRepository userDataRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.userDataRepository = userDataRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DTO_LoginResponse loginUser(DTO_LoginRequest request) throws LoginException {
        // Krok 1: Znajdź użytkownika po emailu
        UserData user = userDataRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("Nie znaleziono użytkownika o podanym adresie email."));

        // Krok 2: Sprawdź poprawność hasła
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new LoginException("Nieprawidłowe hasło.");
        }

        // Krok 3: Utwórz nową sesję
        Sessions newSession = new Sessions();
        newSession.setSession(UUID.randomUUID().toString());
        newSession.setUserData(user);

        // Krok 4: Zapisz sesję w bazie danych
        sessionRepository.save(newSession);

        return new DTO_LoginResponse(
                newSession.getSession(),
                user.getId(),
                user.getUsername(),
                user.getAccountType().name()
        );
    }
}