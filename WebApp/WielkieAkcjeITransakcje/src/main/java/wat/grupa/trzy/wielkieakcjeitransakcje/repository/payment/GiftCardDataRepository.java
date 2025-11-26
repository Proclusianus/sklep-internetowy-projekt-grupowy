package wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.GiftCardData;

import java.util.Optional;

@Repository
public interface GiftCardDataRepository extends JpaRepository<GiftCardData, Long> {
    Optional<GiftCardData> findByCode(String code);
}
