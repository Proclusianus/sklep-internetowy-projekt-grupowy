package wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import java.math.BigDecimal;

public record DTO_Product(Long id, String name, BigDecimal price, String description, String category) {
    public DTO_Product(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getCategory());
    }
}