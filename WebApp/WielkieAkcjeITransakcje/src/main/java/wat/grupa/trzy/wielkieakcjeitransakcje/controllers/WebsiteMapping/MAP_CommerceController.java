package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.WebsiteMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_AddProduct;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.Sell_type;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.ProductService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MAP_CommerceController {

    private final ProductService productService;
    private final SessionCart sessionCart;
    private final ProductRepository productRepository;

    public MAP_CommerceController(ProductService productService, SessionCart sessionCart, ProductRepository productRepository) {
        this.productService = productService;
        this.sessionCart = sessionCart;
        this.productRepository = productRepository;
    }

    @GetMapping("/kupno-sprzedaz")
    public String getBuySellPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> wszystkie = productRepository.findAll();
        List<Product> przefiltrowane;


        if (search != null && !search.isBlank()) {

            przefiltrowane = wszystkie.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.isBlank()) {
            przefiltrowane = wszystkie.stream()
                    .filter(p -> category.equals(p.getCategory()))
                    .collect(Collectors.toList());
            model.addAttribute("selectedCategory", category);
        } else {
            przefiltrowane = wszystkie;
        }


        List<Product> sklepowe = przefiltrowane.stream()
                .filter(p -> p.getSellType() == Sell_type.KUP_TERAZ || p.getSellType() == Sell_type.OBA)
                .collect(Collectors.toList());

        model.addAttribute("producty", sklepowe);
        model.addAttribute("kategorie", productService.getAllCategories()); // To zostawiamy z serwisu
        return "kupnoSprzedaz";
    }

    @GetMapping("/aukcje")
    public String getAuctionsPage(Model model) {

        List<Product> wszystkie = productRepository.findAll();

        List<Product> tylkoAukcje = wszystkie.stream()
                .filter(p -> p.getSellType() != null &&
                        (p.getSellType() == Sell_type.LICYTACJA || p.getSellType() == Sell_type.OBA))
                .collect(Collectors.toList());

        model.addAttribute("producty", tylkoAukcje);
        return "aukcje";
    }

    @GetMapping("/koszyk")
    public String getCartPage(Model model) {
        model.addAttribute("cartItems", sessionCart.getProducts().values());
        return "koszyk";
    }

    // 1. Wyświetlenie formularza
    @GetMapping("/sprzedaj")
    public String getAddProductPage(Model model) {
        model.addAttribute("nowyProduct", new DTO_AddProduct());
        model.addAttribute("kategorie", productService.getAllCategories());
        return "dodajProdukt";
    }

    // 2. Obsługa formularza (POST)
    @PostMapping("/sprzedaj")
    public String processAddProduct(
            @ModelAttribute("nowyProduct") DTO_AddProduct nowyProduct,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Pobieramy zalogowanego użytkownika
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Zapisujemy product
        productService.addProduct(nowyProduct, userDetails.getUser());

        // Przekierowujemy do listy productów z komunikatem sukcesu (opcjonalnie można dodać param)
        return "redirect:/kupno-sprzedaz";
    }

    @PostMapping("/checkout")
    public String processCheckout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";
        }
        return "redirect:/";
    }



}