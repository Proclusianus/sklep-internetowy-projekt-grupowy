package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_CheckoutDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.ShopOrderRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Używamy Mockito
class OrderServiceTest {

    @Mock
    private ShopOrderRepository shopOrderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SessionCart sessionCart;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldSaveOrder_WhenCartIsNotEmpty() {
        // GIVEN
        UserData buyer = new UserData();
        buyer.setId(1L);

        DTO_CheckoutDetails details = new DTO_CheckoutDetails();
        details.setImie("Jan");
        details.setNazwisko("Kowalski");
        details.setTelefon("123456789"); // Upewnij się, że to też jest ustawione

        // Symulujemy product w koszyku
        Long prodId = 10L;
        BigDecimal price = BigDecimal.valueOf(100.50);
        DTO_Product dtoProduct = new DTO_Product(prodId, "Test", price, "Description", "Kat");

        // --- POPRAWKA TUTAJ ---
        // Zamiast Map.of(), używamy HashMap, która pozwala na operację .clear()
        java.util.Map<Long, DTO_Product> products = new java.util.HashMap<>();
        products.put(prodId, dtoProduct);

        when(sessionCart.getProducts()).thenReturn(products);
        // ----------------------

        // Symulujemy znalezienie productu w bazie
        Product dbProduct = new Product();
        dbProduct.setId(prodId);
        dbProduct.setPrice(price);
        when(productRepository.findById(prodId)).thenReturn(java.util.Optional.of(dbProduct));

        // WHEN
        orderService.createOrder(buyer, details);

        // THEN
        verify(shopOrderRepository, times(1)).save(any(ShopOrder.class));

        // Weryfikujemy, czy koszyk faktycznie został wyczyszczony
        // Ponieważ przekazaliśmy prawdziwą HashMapę, metoda .clear() powinna była zadziałać
        assertTrue(products.isEmpty(), "Koszyk powinien być pusty po zamówieniu");
    }
}