package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;

@Controller
@RequestMapping("/cart")
public class REST_CartController {

    private final SessionCart sessionCart;
    private final ProductRepository productRepository;

    public REST_CartController(SessionCart sessionCart, ProductRepository productRepository) {
        this.sessionCart = sessionCart;
        this.productRepository = productRepository;
    }

    @PostMapping("/add/{productId}")
    public String addProductToCart(@PathVariable Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono productu o ID: " + productId));

        sessionCart.addProduct(new DTO_Product(product));

        return "redirect:/kupno-sprzedaz";
    }

    @PostMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable Long productId) {
        sessionCart.removeProduct(productId);

        return "redirect:/koszyk";
    }
}