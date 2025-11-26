package wat.grupa.trzy.wielkieakcjeitransakcje.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.ConfirmationToken;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.ConfirmationTokenRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CleanupServiceTest {

    @Mock private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock private UserDataRepository userDataRepository;

    @InjectMocks
    private CleanupService cleanupService;

    @Test
    @DisplayName("cleanup: Powinien usunąć Użytkownika I Token, jeśli konto jest nieaktywne")
    void cleanup_ShouldDeleteUser_WhenUserIsInactive() {
        // GIVEN
        UserData inactiveUser = new UserData();
        inactiveUser.setId(1L);
        inactiveUser.setEnabled(false); // Użytkownik nieaktywny

        ConfirmationToken expiredToken = new ConfirmationToken();
        expiredToken.setId(100L);
        expiredToken.setUserData(inactiveUser);

        // Mockujemy, że znaleziono jeden wygasły token
        when(confirmationTokenRepository.findByExpiresAtBefore(any())).thenReturn(List.of(expiredToken));

        // WHEN
        cleanupService.cleanupExpiredTokensAndUsers();

        // THEN
        // Skoro user nieaktywny, serwis powinien usunąć USERA (kaskada usunie token)
        verify(userDataRepository, times(1)).delete(inactiveUser);
        // Zazwyczaj nie usuwamy tokenu ręcznie w tej gałęzi if-a, jeśli logika w serwisie jest:
        // if (!active) delete(user) else delete(token)
        verify(confirmationTokenRepository, never()).delete(expiredToken);
    }

    @Test
    @DisplayName("cleanup: Powinien usunąć TYLKO Token, jeśli konto jest już aktywne")
    void cleanup_ShouldDeleteTokenOnly_WhenUserIsActive() {
        // GIVEN
        UserData activeUser = new UserData();
        activeUser.setId(2L);
        activeUser.setEnabled(true); // Użytkownik aktywny (np. aktywował innym tokenem)

        ConfirmationToken expiredToken = new ConfirmationToken();
        expiredToken.setId(200L);
        expiredToken.setUserData(activeUser);

        when(confirmationTokenRepository.findByExpiresAtBefore(any())).thenReturn(List.of(expiredToken));

        // WHEN
        cleanupService.cleanupExpiredTokensAndUsers();

        // THEN
        verify(userDataRepository, never()).delete(activeUser); // Nie wolno usuwać aktywnego usera!
        verify(confirmationTokenRepository, times(1)).delete(expiredToken); // Należy usunąć stary śmieć (token)
    }

    @Test
    @DisplayName("cleanup: Nic nie robi, gdy brak wygasłych tokenów")
    void cleanup_ShouldDoNothing_WhenNoExpiredTokens() {
        // GIVEN
        when(confirmationTokenRepository.findByExpiresAtBefore(any())).thenReturn(Collections.emptyList());

        // WHEN
        cleanupService.cleanupExpiredTokensAndUsers();

        // THEN
        verify(userDataRepository, never()).delete(any());
        verify(confirmationTokenRepository, never()).delete(any());
    }
}