package wat.grupa.trzy.wielkieakcjeitransakcje.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.ConfirmationToken;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.ConfirmationTokenRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class CleanupService {

    private static final Logger log = LoggerFactory.getLogger(CleanupService.class);

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserDataRepository userDataRepository;

    public CleanupService(ConfirmationTokenRepository confirmationTokenRepository, UserDataRepository userDataRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userDataRepository = userDataRepository;
    }

    @Scheduled(fixedRateString = "${cleanup.task.rate.ms}")
    @Transactional
    public void cleanupExpiredTokensAndUsers() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info("Uruchamianie zadania czyszczącego wygasłe tokeny i nieaktywowane konta o {}", now);

        List<ConfirmationToken> expiredTokens = confirmationTokenRepository.findByExpiresAtBefore(now);
        if (expiredTokens.isEmpty()) {
            log.info("Nie znaleziono wygasłych tokenów do usunięcia.");
            return;
        }

        log.info("Znaleziono {} wygasłych tokenów do przetworzenia.", expiredTokens.size());

        for (ConfirmationToken token : expiredTokens) {
            UserData user = token.getUserData();


            if (user != null && !user.isEnabled()) {
                log.warn("Usuwanie nieaktywnego użytkownika {} (ID: {}) powiązanego z wygasłym tokenem.", user.getUsername(), user.getId());
                userDataRepository.delete(user);
            } else {

                log.info("Użytkownik dla tokena {} jest już aktywny lub nie istnieje. Usuwanie samego tokenu.", token.getId());
                confirmationTokenRepository.delete(token);
            }
        }
        log.info("Zakończono zadanie czyszczące.");
    }
}
