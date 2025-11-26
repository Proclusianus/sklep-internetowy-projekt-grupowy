package wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;

@Repository
public interface TransactionDataRepository extends JpaRepository<TransactionData, Long> {
    boolean existsByWallet_IdAndUsedGiftCard_Id(Long walletId, Long giftCardId);
    Page<TransactionData> findAllByWallet_Id(Long walletId, Pageable pageable);
}
