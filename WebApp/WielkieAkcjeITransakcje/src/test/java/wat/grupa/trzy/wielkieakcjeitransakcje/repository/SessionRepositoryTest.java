package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.SessionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deleteBySession_ShouldRemoveSession() {
        // GIVEN
        // Sesja wymaga UserData (zależy od relacji, ale zazwyczaj tak)
        UserData user = new UserData();
        user.setUsername("sessUser");
        user.setEmail("sess@test.com");
        user.setPasswordHash("pass");
        user.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
        entityManager.persist(user);

        Sessions session = new Sessions();
        session.setSession("unique-session-uuid");
        session.setUserData(user);
        entityManager.persist(session);
        entityManager.flush();

        // Upewniamy się, że jest w bazie
        assertThat(sessionRepository.findBySession("unique-session-uuid")).isPresent();

        // WHEN
        sessionRepository.deleteBySession("unique-session-uuid");

        // THEN
        assertThat(sessionRepository.findBySession("unique-session-uuid")).isEmpty();
    }
}