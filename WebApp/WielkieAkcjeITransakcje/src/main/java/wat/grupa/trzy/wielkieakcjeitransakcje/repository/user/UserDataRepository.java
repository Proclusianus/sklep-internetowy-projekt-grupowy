package wat.grupa.trzy.wielkieakcjeitransakcje.repository.user;

import org.springframework.data.jpa.repository.EntityGraph;
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
    boolean existsByBusinessDataFirmName(String firmName);
    boolean existsByBusinessDataNip(String nip);
    boolean existsByBusinessDataKrsId(String krsId);

    @EntityGraph(attributePaths = {
            "personalData",
            "personalData.addresses",
            "businessData",
            "businessData.hqAddress"
    })
    Optional<UserData> findWithDetailsByEmail(String email);
}
