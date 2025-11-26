package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_AddProduct;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.Sell_type;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.AuctionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;

    private final List<String> KATEGORIE = Arrays.asList(
            "Elektronika", "Moda", "Dom i Ogród", "Motoryzacja", "Sport i Turystyka",
            "Kultura i Rozrywka", "Kolekcje i Sztuka", "Dziecko", "Zdrowie i Uroda",
            "Nieruchomości", "Przemysł", "Inne"
    );

    public ProductService(ProductRepository productRepository, AuctionRepository auctionRepository) {
        this.productRepository = productRepository;
        this.auctionRepository = auctionRepository;
    }

    public List<DTO_Product> getAllProducts() {
        return productRepository.findAll().stream().map(DTO_Product::new).collect(Collectors.toList());
    }

    public List<DTO_Product> searchProducts(String keyword) {

        return productRepository.findByNameContainingIgnoreCase(keyword).stream().map(DTO_Product::new).collect(Collectors.toList());
    }

    public List<DTO_Product> getProductsByCategory(String category) {

        return productRepository.findByCategory(category).stream().map(DTO_Product::new).collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return KATEGORIE;
    }

    @Transactional
    public void addProduct(DTO_AddProduct dto, UserData seller) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getCena());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getKategoria());
        product.setSeller(seller);

        // 1. Ustalanie typu sprzedaży
        Sell_type typ;
        try {

            typ = Sell_type.valueOf(dto.getTypSprzedazy());
        } catch (Exception e) {

            typ = Sell_type.KUP_TERAZ;
        }
        product.setSellType(typ);

        // 2. Zapisz produkt (żeby miał ID)
        productRepository.save(product);

        // 3. Jeśli to aukcja -> utwórz encję Auction
        if (typ == Sell_type.LICYTACJA || typ == Sell_type.OBA) {
            Auction auction = new Auction();
            auction.setProduct(product);
            auction.setAktywna(true);
            auction.setCenaWywolawcza(product.getPrice());
            auction.setAktualnaCena(product.getPrice());


            auction.setDataZakonczenia(LocalDateTime.now().plusDays(7));

            auctionRepository.save(auction);
        }
    }
}