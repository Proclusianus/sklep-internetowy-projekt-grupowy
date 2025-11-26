package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import java.util.List;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    // ZMIANA: name metody po angielsku (findByBuyer...)
    List<ShopOrder> findByBuyerOrderByOrderDateDesc(UserData buyer);
}