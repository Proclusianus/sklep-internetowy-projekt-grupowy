package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
