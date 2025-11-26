package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApiLogoutServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private ApiLogoutService apiLogoutService;

    @Test
    void logout_ShouldDeleteSession_WhenCalled() {
        // GIVEN
        String sessionId = "some-uuid-session";

        // WHEN
        apiLogoutService.logout(sessionId);

        // THEN
        // Weryfikujemy, czy metoda deleteBySession została wywołana dokładnie raz z podanym ID
        verify(sessionRepository, times(1)).deleteBySession(sessionId);
    }
}