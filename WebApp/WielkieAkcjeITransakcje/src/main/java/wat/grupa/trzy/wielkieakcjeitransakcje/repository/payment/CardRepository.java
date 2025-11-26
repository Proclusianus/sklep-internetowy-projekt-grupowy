package wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;

public interface CardRepository extends JpaRepository<CardData, Long> {
}
