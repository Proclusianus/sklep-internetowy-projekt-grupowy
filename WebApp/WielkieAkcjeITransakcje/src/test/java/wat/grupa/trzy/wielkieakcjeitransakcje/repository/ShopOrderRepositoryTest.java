package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ShopOrderRepositoryTest {

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByBuyerOrderByOrderDateDesc_ShouldReturnOrderedList() {
        // GIVEN - Musimy najpierw stworzyć użytkownika (Kupującego)
        UserData buyer = new UserData();
        buyer.setUsername("buyer");
        buyer.setEmail("buyer@test.com");
        buyer.setPasswordHash("pass");
        buyer.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
        entityManager.persist(buyer);

        // Zamówienie 1 (Starsze)
        ShopOrder order1 = new ShopOrder();
        order1.setBuyer(buyer);
        order1.setOrderDate(LocalDateTime.now().minusDays(2));
        order1.setTotalAmount(BigDecimal.valueOf(100));
        order1.setStatus("OLD");
        entityManager.persist(order1);

        // Zamówienie 2 (Nowsze)
        ShopOrder order2 = new ShopOrder();
        order2.setBuyer(buyer);
        order2.setOrderDate(LocalDateTime.now());
        order2.setTotalAmount(BigDecimal.valueOf(200));
        order2.setStatus("NEW");
        entityManager.persist(order2);

        // WHEN
        List<ShopOrder> orders = shopOrderRepository.findByBuyerOrderByOrderDateDesc(buyer);

        // THEN
        assertThat(orders).hasSize(2);
        // Sprawdzamy kolejność - nowsze (order2) powinno być pierwsze
        assertThat(orders.get(0).getStatus()).isEqualTo("NEW");
        assertThat(orders.get(1).getStatus()).isEqualTo("OLD");
    }
}