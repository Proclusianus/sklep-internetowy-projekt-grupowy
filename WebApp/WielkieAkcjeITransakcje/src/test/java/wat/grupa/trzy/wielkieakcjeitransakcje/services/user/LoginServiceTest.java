package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginRequest;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginResponse;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserDataRepository userDataRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void loginUser_ShouldReturnDto_WhenCredentialsAreCorrect() throws LoginException {
        // GIVEN
        String email = "user@test.com";
        String rawPassword = "password";
        String encodedPassword = "encoded_password";

        DTO_LoginRequest request = new DTO_LoginRequest(email, rawPassword);

        UserData user = new UserData();
        user.setId(1L);
        user.setEmail(email);
        user.setUsername("username");
        user.setPasswordHash(encodedPassword);
        user.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);

        when(userDataRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        // Mockujemy zapis, zwracając ten sam obiekt
        when(sessionRepository.save(any(Sessions.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        // Tutaj oczekujemy DTO_LoginResponse, bo tak zmieniliśmy LoginService
        DTO_LoginResponse response = loginService.loginUser(request);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getSessionId());
        // POPRAWKA: Używamy getterów zamiast bezpośredniego dostępu
        assertEquals("username", response.getUsername());
        verify(sessionRepository, times(1)).save(any(Sessions.class));
    }

    @Test
    void loginUser_ShouldThrowException_WhenUserNotFound() {
        // GIVEN
        DTO_LoginRequest request = new DTO_LoginRequest("unknown@test.com", "pass");
        when(userDataRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(LoginException.class, () -> loginService.loginUser(request));
    }

    @Test
    void loginUser_ShouldThrowException_WhenPasswordIsWrong() {
        // GIVEN
        UserData user = new UserData();
        user.setPasswordHash("correct_hash");

        when(userDataRepository.findByEmail(any())).thenReturn(Optional.of(user));
        // Hasło się nie zgadza
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        DTO_LoginRequest request = new DTO_LoginRequest("test@test.com", "wrong_pass");

        // WHEN & THEN
        assertThrows(LoginException.class, () -> loginService.loginUser(request));
    }
}