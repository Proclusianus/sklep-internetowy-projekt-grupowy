package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_CheckoutDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.OrderItem;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.ShopOrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final ProductRepository productRepository;
    private final SessionCart sessionCart;

    public OrderService(ShopOrderRepository shopOrderRepository, ProductRepository productRepository, SessionCart sessionCart) {
        this.shopOrderRepository = shopOrderRepository;
        this.productRepository = productRepository;
        this.sessionCart = sessionCart;
    }

    @Transactional
    public Long createOrder(UserData buyer, DTO_CheckoutDetails details) {
        Map<Long, DTO_Product> productsInCart = sessionCart.getProducts();

        if (productsInCart.isEmpty()) {
            throw new IllegalStateException("Koszyk jest pusty!");
        }

        ShopOrder order = new ShopOrder();
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NIEOPLACONE");

        String addressSnapshot = String.format("%s %s\n%s %s\n%s %s\nTel: %s",
                details.getImie(), details.getNazwisko(),
                details.getUlica(), details.getNumerDomu(),
                details.getKodPocztowy(), details.getMiasto(),
                details.getTelefon());
        order.setDeliveryAddressSnapshot(addressSnapshot);

        BigDecimal total = BigDecimal.ZERO;
        for (DTO_Product dto : productsInCart.values()) {
            Product productDb = productRepository.findById(dto.id())
                    .orElseThrow(() -> new IllegalStateException("Produkt nie istnieje"));

            OrderItem item = new OrderItem();
            item.setShopOrder(order);
            item.setProduct(productDb);
            item.setPriceAtPurchase(productDb.getPrice());

            order.getOrderItems().add(item);
            total = total.add(productDb.getPrice());
        }
        order.setTotalAmount(total);

        shopOrderRepository.save(order);

        sessionCart.getProducts().clear();

        return order.getId();
    }

    public List<ShopOrder> getUserOrders(UserData user) {
        return shopOrderRepository.findByBuyerOrderByOrderDateDesc(user);
    }

    public ShopOrder getOrderDetails(Long orderId, UserData user) {
        ShopOrder order = shopOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zamówienia"));

        if (!order.getBuyer().getId().equals(user.getId())) {
            throw new SecurityException("Brak dostępu do tego zamówienia");
        }
        return order;
    }

    public ShopOrder getOrderById(Long id) {
        return shopOrderRepository.findById(id).orElseThrow();
    }
}