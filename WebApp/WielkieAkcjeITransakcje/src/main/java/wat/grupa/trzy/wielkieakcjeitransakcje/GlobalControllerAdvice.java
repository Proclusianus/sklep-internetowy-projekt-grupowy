package wat.grupa.trzy.wielkieakcjeitransakcje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Mówi Springowi, że ta klasa "doradza" wszystkim kontrolerom
public class GlobalControllerAdvice {

    private final SessionCart sessionCart;

    @Autowired
    public GlobalControllerAdvice(SessionCart sessionCart) {
        this.sessionCart = sessionCart;
    }

    // Ta metoda uruchomi się PRZED każdym zapytaniem do dowolnego kontrolera
    @ModelAttribute("cartItemCount")
    public int getCartItemCount() {
        // Dodaje do modelu atrybut "cartItemCount", dostępny w każdym pliku HTML
        return sessionCart.getProducts().size();
    }

    @ModelAttribute("cartItemsPreview")
    public List<DTO_Product> getCartItemsPreview() {
        // Dodaje listę productów z koszyka (do 4 sztuk) do modelu
        return sessionCart.getProducts().values().stream()
                .limit(4) // Ograniczamy do 4 productów
                .collect(Collectors.toList());
    }
}