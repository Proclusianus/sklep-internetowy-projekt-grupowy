package wat.grupa.trzy.wielkieakcjeitransakcje.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Sessions, Long> {
    Optional<Sessions> findBySession(String session);
    void deleteBySession(String session);
 }