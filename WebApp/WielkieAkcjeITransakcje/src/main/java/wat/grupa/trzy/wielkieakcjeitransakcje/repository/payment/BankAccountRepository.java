package wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;

public interface BankAccountRepository extends JpaRepository<BankAccountData, Long> {
}
