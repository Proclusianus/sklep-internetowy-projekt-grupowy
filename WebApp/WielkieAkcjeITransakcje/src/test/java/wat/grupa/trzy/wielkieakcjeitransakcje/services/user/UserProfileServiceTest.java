package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_UserProfile;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    void getUserProfileBySessionId_ShouldReturnProfile_WhenSessionExists() throws AccessDeniedException {
        // GIVEN
        String sessionId = "valid-session-id";

        UserData user = new UserData();
        user.setId(10L);
        user.setEmail("test@test.com");
        user.setUsername("testUser");
        user.setAccountType(E_TYP_KONTA.FIRMA);

        Sessions session = new Sessions();
        session.setSession(sessionId);
        session.setUserData(user);

        when(sessionRepository.findBySession(sessionId)).thenReturn(Optional.of(session));

        // WHEN
        DTO_UserProfile result = userProfileService.getUserProfileBySessionId(sessionId);

        // THEN
        assertNotNull(result);
        // POPRAWKA: Używamy getterów zamiast stylu rekordów
        assertEquals("testUser", result.getUsername());
        assertEquals("FIRMA", result.getAccountType());
    }

    @Test
    void getUserProfileBySessionId_ShouldThrowException_WhenSessionInvalid() {
        // GIVEN
        String sessionId = "invalid-id";
        when(sessionRepository.findBySession(sessionId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(AccessDeniedException.class, () -> {
            userProfileService.getUserProfileBySessionId(sessionId);
        });
    }
}