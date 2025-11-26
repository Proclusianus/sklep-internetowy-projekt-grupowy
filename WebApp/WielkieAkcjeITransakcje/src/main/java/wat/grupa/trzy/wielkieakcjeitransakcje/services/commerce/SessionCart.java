package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class SessionCart {
    private final Map<Long, DTO_Product> products = new HashMap<>();

    public void addProduct(DTO_Product product) {
        products.put(product.id(), product);
    }

    public void removeProduct(Long productId) {
        products.remove(productId);
    }

    public Map<Long, DTO_Product> getProducts() {
        return products;
    }
}