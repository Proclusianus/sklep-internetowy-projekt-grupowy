package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Używa konfiguracji H2
class UserDataRepositoryTest {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private TestEntityManager entityManager; // Pomocnik do łatwego wstawiania danych w testach

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // GIVEN
        UserData user = new UserData();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hash");
        user.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
        entityManager.persist(user); // Zapisz bezpośrednio do bazy testowej
        entityManager.flush();

        // WHEN
        Optional<UserData> found = userDataRepository.findByEmail("test@example.com");

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUsernameTaken() {
        // GIVEN
        UserData user = new UserData();
        user.setUsername("zajety_login");
        user.setEmail("email@email.com");
        user.setPasswordHash("hash");
        user.setAccountType(E_TYP_KONTA.FIRMA);
        entityManager.persist(user);

        // WHEN
        boolean exists = userDataRepository.existsByUsername("zajety_login");

        // THEN
        assertThat(exists).isTrue();
    }
}